# cog-algorithms

If you have Java installed on your computer, you can run the program from the console/terminal.

AC3Main.java includes the source code and it reads the contents of the problem from "arcs.txt". Hence, the .java file and the .txt file must be in the same folder.

Commands are as below: <br />
javac AC3Main.java <br />
java AC3Main <br />

Some output information will be written to the console and also a new file called "results.txt" will be generated.

In the "arcs.txt" file, first list all the junctions along with their types such as:<br />
1,A<br />
2,L<br />

When you are finished with them, put & and list the edges that are equivalent using the hands notation given on the last page of Steedman's chapter.

1,A<br />
2,L<br />
3,T<br />
&<br />
1,2,3,1<br /> 
and so on<br />

This means that 1st junction's 2nd hand is equivalent to the 3rd junction's 1st hand. Therefore, they should be assigned the same label.

In the "results.txt" file, you will first see the possible labels satisfying constraints for each junction after the AC3 algorithm. Next, a backtracking search is applied and a possible interpretation of the image will be given, if there exists at least one. (If there are more than one interpretation, this program will only output one of them).


