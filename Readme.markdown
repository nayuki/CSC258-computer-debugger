CSC258 computer debugger
========================

Introduction
------------

The CSC258 computer is a hypothetical machine created by Eric Hehner at the
University of Toronto. Officially, its behavior and assembly language syntax are
specified.

This package is an independent, unofficial implementation of the CSC258
computer, containing a rich set of software tools and sample programs.
It was developed from scratch by Nayuki in 2010, and is open source (see license below).
The tools are written in Java, requiring version 1.6 or above.

Home page: https://www.nayuki.io/page/csc258-computer-debugger


Features
--------

User features:

* Command-line runner (`csc258comp.runner.Runner`)
* Graphical debugger (Swing UI) (`csc258comp.debugger.Debugger`)
  * State of registers
  * Execution step, backward-step, run, suspend
  * Breakpoints
  * Source code
  * Current instruction
  * Memory contents
* Meaningful error messages from the compiler
* Sample programs (in the "demos" directory)

Developer features:

* Clean code, easily modifiable and extensible
* Code guards against null pointers, out-of-bounds, illegal input parameters,
  and other misuses of the API in a fail-fast way
* Some functionality is covered by JUnit tests
* Compilation of CSC258 programs is separated from linking


Building
--------

Run `javac` on every `.java` file. Or better yet, import the project into an IDE
like Eclipse or NetBeans.

On Unix, you can also perform these commands in a shell:

    cd /...your path to.../csc258
    mkdir bin
    javac -d bin $(find src -name "*.java" -print)


Running
-------

You must set the working directory or the class path to the appropriate root for
Java class files. For example, if the runner is located at
`/the/package/csc258comp/runner/Csc258Runner.class`, then you must do one of
these (just one):

* `cd /the/package`  
  `java csc258comp.runner.Runner` *[CSC258 files]*
* `java -cp /the/package csc258comp.runner.Runner` *[CSC258 files]*
* `export CLASSPATH=/the/package`  
  `java csc258comp.runner.Runner` *[CSC258 files]*

Examples:

* `java csc258comp.runner.Runner demos/helloworld.258.txt`
* `java csc258comp.debugger.Debugger demos/primes.258.txt demos/lib/const.258.txt demos/lib/printInt.258.txt`


License
-------

Copyright © 2016 Project Nayuki  
https://www.nayuki.io/page/csc258-computer-debugger

Licensed under the GNU Public License v3.0+ (see COPYING.txt)
