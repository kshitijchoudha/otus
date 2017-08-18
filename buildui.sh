#/usr/bin/bash

cd otus-ui/otus-react
npm install
npm run build
webpack
cd ..
mkdir -p src/main/resources/static
cp -r otus-react/dist/* src/main/resources/static