'use strict';

import _ from 'lodash';
import { Alert } from 'react-bootstrap';
import React from 'react';
import TWEEN from 'tween.js'; // Start TWEEN updates for sparklines and loading screen fading out
import Vizceral from 'vizceral-react';
import 'vizceral-react/dist/vizceral.css';
import keypress from 'keypress.js';
import queryString from 'query-string';
import request from 'superagent';
import Toggle from 'react-toggle';
import 'react-toggle/style.css';

import './trafficFlow.css';
import Breadcrumbs from './breadcrumbs';
// import DisplayOptions from './displayOptions';
// import PhysicsOptions from './physicsOptions';
// import FilterControls from './filterControls';
import DetailsPanelConnection from './detailsPanelConnection';
import DetailsPanelNode from './detailsPanelNode';
import LoadingCover from './loadingCover';
// import Locator from './locator';
// import OptionsPanel from './optionsPanel';
import UpdateStatus from './updateStatus';

import filterActions from './filterActions';
import filterStore from './filterStore';

const listener = new keypress.Listener();

const hasOwnPropFunc = Object.prototype.hasOwnProperty;

function animate (time) {
  requestAnimationFrame(animate);
  TWEEN.update(time);
}
requestAnimationFrame(animate);

const panelWidth = 400;

class TrafficFlow extends React.Component {
  constructor (props) {
    super(props);

    this.state = {
      isDemo: false,
      currentView: undefined,
      redirectedFrom: undefined,
      selectedChart: undefined,
      displayOptions: {
        allowDraggingOfNodes: false,
        showLabels: true
      },
      currentGraph_physicsOptions: {
        isEnabled: true,
        viscousDragCoefficient: 0.2,
        hooksSprings: {
          restLength: 50,
          springConstant: 0.2,
          dampingConstant: 0.1
        },
        particles: {
          mass: 1
        }
      },
      labelDimensions: {},
      appliedFilters: filterStore.getChangedFilters(),
      filters: filterStore.getFiltersArray(),
      searchTerm: '',
      matches: {
        total: -1,
        visible: -1
      },
      trafficData: {
        nodes: [],
        connections: []
      },
      regionUpdateStatus: [],
      timeOffset: 0,
      modes: {
        detailedNode: 'volume'
      }
    };

    this.vizceralStyles = {
      colorText: 'rgb(214, 214, 214)',
      colorTextDisabled: 'rgb(129, 129, 129)',
      colorTraffic: {
        normal: 'rgb(186, 213, 237)',
        normalDonut: 'rgb(91, 91, 91)',
        warning: 'rgb(268, 185, 73)',
        danger: 'rgb(184, 36, 36)',
      },
      colorNormalDimmed: 'rgb(101, 117, 128)',
      colorBackgroundDark: 'rgb(35, 35, 35)',
      colorLabelBorder: 'rgb(16, 17, 18)',
      colorLabelText: 'rgb(0, 0, 0)',
      colorDonutInternalColor: 'rgb(35, 35, 35)',
      colorDonutInternalColorHighlighted: 'rgb(255, 255, 255)',
      colorConnectionLine: 'rgb(144, 221, 88)',
      colorPageBackground: 'rgb(45, 45, 45)',
      colorPageBackgroundTransparent: 'rgba(45, 45, 45, 0)',
      colorBorderLines: 'rgb(137, 137, 137)',
      colorArcBackground: 'rgb(60, 60, 60)'
    };

    // Browser history support
    window.addEventListener('popstate', event => this.handlePopState(event.state));

    // Keyboard interactivity
    listener.simple_combo('esc', () => {
      if (this.state.detailedNode) {
        this.setState({ detailedNode: undefined });
      } else if (this.state.currentView.length > 0) {
        this.setState({ currentView: this.state.currentView.slice(0, -1) });
      }
    });
  }

  handlePopState () {
    const state = window.history.state || {};
    this.poppedState = true;
    this.setState({ currentView: state.selected, objectToHighlight: state.highlighted });
  }

  viewChanged = (data) => {
    const changedState = {
      currentView: data.view,
      searchTerm: '',
      matches: { total: -1, visible: -1 },
      redirectedFrom: data.redirectedFrom
    };
    if (hasOwnPropFunc.call(data, 'graph')) {
      let oldCurrentGraph = this.state.currentGraph;
      if (oldCurrentGraph == null) oldCurrentGraph = null;
      let newCurrentGraph = data.graph;
      if (newCurrentGraph == null) newCurrentGraph = null;
      if (oldCurrentGraph !== newCurrentGraph) {
        changedState.currentGraph = newCurrentGraph;
        const o = newCurrentGraph === null ? null : newCurrentGraph.getPhysicsOptions();
        changedState.currentGraph_physicsOptions = o;
      }
    }
    this.setState(changedState);
  }

  viewUpdated = () => {
    this.setState({});
  }

  objectHighlighted = (highlightedObject) => {
    // need to set objectToHighlight for diffing on the react component. since it was already highlighted here, it will be a noop
    this.setState({ highlightedObject: highlightedObject, objectToHighlight: highlightedObject ? highlightedObject.getName() : undefined, searchTerm: '', matches: { total: -1, visible: -1 }, redirectedFrom: undefined });
  }

  nodeContextSizeChanged = (dimensions) => {
    this.setState({ labelDimensions: dimensions });
  }

  checkInitialRoute () {
    // Check the location bar for any direct routing information
    const pathArray = window.location.pathname.split('/');
    const currentView = [];
    if (pathArray[1]) {
      currentView.push(pathArray[1]);
      if (pathArray[2]) {
        currentView.push(pathArray[2]);
      }
    }
    const parsedQuery = queryString.parse(window.location.search);

    this.setState({ currentView: currentView, objectToHighlight: parsedQuery.highlighted });
  }

  beginSampleData = () => {
    this.traffic = { nodes: [], connections: [] };
    const url = this.state.isDemo ? '/sample_data_original.json' : '/service-aggregate/data/';

    request.get(url)
      .set('Accept', 'application/json')
      .end((err, res) => {
        if (res && res.status === 200) {
          this.traffic.clientUpdateTime = Date.now();
          this.updateData(res.body);
        }
      });
  }

  bringServiceUpDown = (name, upOrDown) => {
    const endpointMap = {
      UP: 'activate',
      DOWN: 'deactivate'
    };

    const callServiceName = name.toLowerCase();

    request.get(`/service-aggregate/service-${endpointMap[upOrDown]}/${callServiceName}`)
      .set('Accept', 'application/json')
      .end((err, res) => {
        if (res && res.status === 200) {
          setTimeout(() => {
            this.beginSampleData();
          }, 5000);
        }
      });
  }

  componentDidMount () {
    this.checkInitialRoute();
    this.beginSampleData();

    // Listen for changes to the stores
    filterStore.addChangeListener(this.filtersChanged);
  }

  componentWillUnmount () {
    filterStore.removeChangeListener(this.filtersChanged);
  }

  shouldComponentUpdate (nextProps, nextState) {
    if (!this.state.currentView ||
        this.state.currentView[0] !== nextState.currentView[0] ||
        this.state.currentView[1] !== nextState.currentView[1] ||
        this.state.highlightedObject !== nextState.highlightedObject) {
      const titleArray = (nextState.currentView || []).slice(0);
      titleArray.unshift('Project Otus');
      document.title = titleArray.join(' / ');

      if (this.poppedState) {
        this.poppedState = false;
      } else if (nextState.currentView) {
        const highlightedObjectName = nextState.highlightedObject && nextState.highlightedObject.getName();
        const state = {
          title: document.title,
          url: nextState.currentView.join('/') + (highlightedObjectName ? `?highlighted=${highlightedObjectName}` : ''),
          selected: nextState.currentView,
          highlighted: highlightedObjectName
        };
        window.history.pushState(state, state.title, state.url);
      }
    }
    return true;
  }

  updateData (newTraffic) {
    const regionUpdateStatus = _.map(_.filter(newTraffic.nodes, n => n.name !== 'INTERNET'), (node) => {
      const updated = node.updated;
      return { region: node.name, updated: updated };
    });
    const lastUpdatedTime = _.max(_.map(regionUpdateStatus, 'updated'));
    this.setState({
      regionUpdateStatus: regionUpdateStatus,
      timeOffset: newTraffic.clientUpdateTime - newTraffic.serverUpdateTime,
      lastUpdatedTime: lastUpdatedTime,
      trafficData: newTraffic
    });
  }

  isSelectedNode () {
    return this.state.currentView && this.state.currentView[1] !== undefined;
  }

  zoomCallback = () => {
    const currentView = _.clone(this.state.currentView);
    if (currentView.length === 1 && this.state.focusedNode) {
      currentView.push(this.state.focusedNode.name);
    } else if (currentView.length === 2) {
      currentView.pop();
    }
    this.setState({ currentView: currentView });
  }

  displayOptionsChanged = (options) => {
    const displayOptions = _.merge({}, this.state.displayOptions, options);
    this.setState({ displayOptions: displayOptions });
  }

  physicsOptionsChanged = (physicsOptions) => {
    this.setState({ currentGraph_physicsOptions: physicsOptions });
    let currentGraph = this.state.currentGraph;
    if (currentGraph == null) currentGraph = null;
    if (currentGraph !== null) {
      currentGraph.setPhysicsOptions(physicsOptions);
    }
  }

  navigationCallback = (newNavigationState) => {
    this.setState({ currentView: newNavigationState });
  }

  detailsClosed = () => {
    // If there is a selected node, deselect the node
    if (this.isSelectedNode()) {
      this.setState({ currentView: [this.state.currentView[0]] });
    } else {
      // If there is just a detailed node, remove the detailed node.
      this.setState({ focusedNode: undefined, highlightedObject: undefined });
    }
  }

  filtersChanged = () => {
    this.setState({
      appliedFilters: filterStore.getChangedFilters(),
      filters: filterStore.getFiltersArray()
    });
  }

  filtersCleared = () => {
    if (!filterStore.isClear()) {
      if (!filterStore.isDefault()) {
        filterActions.resetFilters();
      } else {
        filterActions.clearFilters();
      }
    }
  }

  locatorChanged = (value) => {
    this.setState({ searchTerm: value });
  }

  matchesFound = (matches) => {
    this.setState({ matches: matches });
  }

  nodeClicked = (node) => {
    if (this.state.currentView.length === 1) {
      // highlight node
      this.setState({ objectToHighlight: node.getName() });
    } else if (this.state.currentView.length === 2) {
      // detailed view of node
      this.setState({ currentView: [this.state.currentView[0], node.getName()] });
    }
  }

  resetLayoutButtonClicked = () => {
    const g = this.state.currentGraph;
    if (g != null) {
      g._relayout();
    }
  }

  dismissAlert = () => {
    this.setState({ redirectedFrom: undefined });
  }

  toggleDemoMode = () => {
    this.setState(prevState => ({
      isDemo: !prevState.isDemo
    }));

    setTimeout(() => {
      this.beginSampleData();
    }, 1000);
  }

  render () {
    const globalView = this.state.currentView && this.state.currentView.length === 0;
    const nodeView = !globalView && this.state.currentView && this.state.currentView[1] !== undefined;
    let nodeToShowDetails = this.state.currentGraph && this.state.currentGraph.focusedNode;
    nodeToShowDetails = nodeToShowDetails || (this.state.highlightedObject && this.state.highlightedObject.type === 'node' ? this.state.highlightedObject : undefined);
    const nodeStatus = nodeToShowDetails && nodeToShowDetails['service-details'].status;
    const connectionToShowDetails = this.state.highlightedObject && this.state.highlightedObject.type === 'connection' ? this.state.highlightedObject : undefined;
    const showLoadingCover = !this.state.currentGraph;

    // let matches;
    // if (this.state.currentGraph) {
    //   matches = {
    //     totalMatches: this.state.matches.total,
    //     visibleMatches: this.state.matches.visible,
    //     total: this.state.currentGraph.nodeCounts.total,
    //     visible: this.state.currentGraph.nodeCounts.visible
    //   };
    // }

    return (
      <div className="vizceral-container">
        { this.state.redirectedFrom ?
          <Alert onDismiss={this.dismissAlert}>
            <strong>{this.state.redirectedFrom.join('/') || '/'}</strong> does not exist, you were redirected to <strong>{this.state.currentView.join('/') || '/'}</strong> instead
          </Alert>
        : undefined }
        <div className="subheader">
          <Breadcrumbs rootTitle="global" navigationStack={this.state.currentView || []} navigationCallback={this.navigationCallback} />
          <UpdateStatus status={this.state.regionUpdateStatus} baseOffset={this.state.timeOffset} warnThreshold={180000} />
          <div style={{ float: 'right', paddingTop: '4px' }}>
            {/* <OptionsPanel title="Filters"><FilterControls /></OptionsPanel>*/}
            {/* <OptionsPanel title="Display"><DisplayOptions options={this.state.displayOptions} changedCallback={this.displayOptionsChanged} /></OptionsPanel>*/}
            {/* <OptionsPanel title="Physics"><PhysicsOptions options={this.state.currentGraph_physicsOptions} changedCallback={this.physicsOptionsChanged}/></OptionsPanel>*/}
            {/* <a role="button" className="reset-layout-link" onClick={this.resetLayoutButtonClicked}>Reset Layout</a>*/}
            <label>
              <Toggle
                defaultChecked={this.state.isDemo}
                onChange={this.toggleDemoMode} />
            </label>
          </div>
        </div>
        <div className="service-traffic-map">
          <div style={{ position: 'absolute', top: '0px', right: nodeToShowDetails || connectionToShowDetails ? '380px' : '0px', bottom: '0px', left: '0px' }}>
            <Vizceral traffic={this.state.trafficData}
                      view={this.state.currentView}
                      showLabels={this.state.displayOptions.showLabels}
                      filters={this.state.filters}
                      viewChanged={this.viewChanged}
                      viewUpdated={this.viewUpdated}
                      objectHighlighted={this.objectHighlighted}
                      nodeContextSizeChanged={this.nodeContextSizeChanged}
                      objectToHighlight={this.state.objectToHighlight}
                      matchesFound={this.matchesFound}
                      match={this.state.searchTerm}
                      modes={this.state.modes}
                      allowDraggingOfNodes={this.state.displayOptions.allowDraggingOfNodes}
                      styles={this.vizceralStyles}
            />
          </div>
          {
            !!nodeToShowDetails &&
            <DetailsPanelNode node={nodeToShowDetails}
                              nodeStatus={nodeStatus}
                              nodeSelected={nodeView}
                              region={this.state.currentView[0]}
                              width={panelWidth}
                              zoomCallback={this.zoomCallback}
                              closeCallback={this.detailsClosed}
                              reloadCallback={(name, upOrDown) => this.bringServiceUpDown(name, upOrDown)}
                              nodeClicked={node => this.nodeClicked(node)}
            />
          }
          {
            !!connectionToShowDetails &&
            <DetailsPanelConnection connection={connectionToShowDetails}
                                    region={this.state.currentView[0]}
                                    width={panelWidth}
                                    closeCallback={this.detailsClosed}
                                    nodeClicked={node => this.nodeClicked(node)}
            />
          }
          <LoadingCover show={showLoadingCover} />
        </div>
      </div>
    );
  }
}

TrafficFlow.propTypes = {
};

export default TrafficFlow;
