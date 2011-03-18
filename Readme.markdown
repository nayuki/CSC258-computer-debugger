CSC258 Computer
===============

Introduction
------------

The CSC258 computer is a hypothetical machine created by Eric Hehner at the
University of Toronto. Officially, its behavior and assembly language syntax are
specified.

This package is an independent, unofficial implementation of the CSC258
computer, containing a rich set of software tools and sample programs. It was
developed from scratch by Nayuki Minase in 2010, and is licensed under the MIT
License (open source) (see below). The tools are written in Java, requiring
version 1.6 or above.


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

* `cd /the/package  
  java csc258comp.runner.Runner` *[CSC258 files]*
* `java -cp /the/package csc258comp.runner.Runner` *[CSC258 files]*
* `export CLASSPATH=/the/package`  
  `java csc258comp.runner.Runner` *[CSC258 files]*

Examples:

* `java csc258comp.runner.Runner demos/helloworld.258.txt`
* `java csc258comp.debugger.Debugger demos/primes.258.txt demos/lib/const.258.txt demos/lib/printInt.258.txt`


License
-------

(MIT License)

© Copyright 2010 Nayuki Minase

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

* The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
