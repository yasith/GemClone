###Compile With

`mkdir obj`

`javac -d obj -cp lib/slick.jar:lib/lwjgl.jar:lib/jorbis-0.0.15.jar:lib/jogg-0.0.7.jar src/gemclone/*.java`

###Run With

`java -Djava.library.path="lib" -cp obj:lib/slick.jar:lib/lwjgl.jar:lib/jorbis-0.0.15.jar:lib/jogg-0.0.7.jar gemclone.Game`
