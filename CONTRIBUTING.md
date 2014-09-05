Contributing to Amethyst
========================

Code standards
--------------

* LF only line endings
* Spaces, no tabs please.
* Avoid trailing whitespace
* Avoid unnecessary usages of "this"
* On block statements, curly braces should be on the same line as the opening statement with one space.
* Constant names should be all caps
* Conditionals should have one space between the statement and the conditional
* Argument definitions should have no spaces between the brackets and arguments
* Don't worry too much about line length - if using a builder or similar, separate lines, but in general weird mid-statement line breaks are a bit silly.
* Order access modifiers followed by final/volatile/transient followed by static 

How to get your feature included
--------------------------------

* Make sure your idea fits the goals of the project. The best thing to do is to scan through the currently open issues, and make a comment.
* Fork the repository, create a new branch named ```feature/featureName``` where featureName is the name of the feature you are working on
* Implement your feature, and submit to the feature branch
* Open a pull request to the ```develop``` branch, with a brief description of what your change does and how it benefits the project.

How to get your bugfix included
-------------------------------

* Make sure the bug is not intended behaviour
* Fork the repository, create a new branch named ```bugfix/bugName``` where bugName is the name of the bug you are fixing
* Implement your bugfix, and submit it to the bugfix branch
* Open a pull request to the ```develop``` branch, including clear instructions on how to reproduce the bug.
