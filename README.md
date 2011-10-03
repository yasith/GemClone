#Compile With

`mkdir obj`
`javac -d obj -cp lib/slick.jar:lib/lwjgl.jar src/gemclone/*.java`

#Run With

`java -Djava.library.path="lib" -cp obj:lib/slick.jar:lib/lwjgl.jar gemclone.Game`