## How to run project
1. Open Terminal
2. ``` ./schema.sh -h ```

## Need to see the docs?
1. ```gem instal yard```
2. ```yard doc model_generation```
3. check the doc folder

## How to add a new generated file to xcode?
1. Open ```model_generation``` with an editor of your choice and open the ```main.rb``` file.
2. Edit the method of swift generation by adding the name of the new file on the ```ready_to_prod``` array
3. In Xcode go to ``` Schema/Sources/CodeGeneration/BeagleSchemaGenerated``` and toggle the option show in finder. The new generated file will be there.
4. Drag the file to ```BeagleSchemaGenerated``` **as a group** to Xcode.
5. Do not forget to remove old files, if there are any. 😊

P.S.: If you are a swift developer, you will have to install sourcery. (This is temporary)

## IMPORTANT

**Be very carefull** when running schema with the ```-a``` option since it will regenerate models for all the languages. Only use this option if you are not writing a breaking change.

If you are writing a **breaking change** it's important that **at least** one developer of each affected platform check the pr.

## We have some stuff to think about
- [ ] can we easily integrate ruby with another language that is quicker to understand? Do we really need to do that?
- [ ] Unit tests
- [ ] Generation Tests
- [ ] Integrate License Header Manager(this is a plugin in VSCode) to folder Schema
