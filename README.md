# TractiveTest
Document Questions:
- Do you expect involvement of other dev teams (besides mobile)?

  Yes, 
  The collar bluetooth api and its charactristic UUID should come from device developers (or more likely documentation they prepare).
  UI/UX is usually created by an Experience team.
  
  
- How would you setup the deployment to the play store

  We need to setup proper keys to sign development bundles or assemble apks. 
  Since playstore only accepts bundles we can set up a task to automatically bundle flavors we would like to deploy, this task would also be part of CI/CD to validate builds.
  Furthermore we can use CircleCI to automate deployment although I do not have any experience with the aforementioned tool.
