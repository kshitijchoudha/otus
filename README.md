# Otus

see if you can run it with
 `java -jar otus-ui-1.0-SNAPSHOT.jar`
 
 ## ui build steps:
 1. install nodejs
 3. `cd otus-ui/otus-react`
 4. `npm install`
 5. `npm run build`
 6. `webpack`
 7. `cd ..`
 8. `mkdir -p src/main/resources/static`
 9. `cp -r otus-react/dist/* src/main/resources/static`
 10. `mvn package`
