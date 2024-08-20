# babooka

What is Babashka?

From a user perspective, babashka is a scripting runtime for the Clojure programming language. It lets you execute Clojure programs in contexts where you’d typically use bash, ruby, python, and the like. Use cases include build scripts, command line utilities, small web applications, git hooks, AWS Lambda functions, and everywhere you want to use Clojure where fast startup and/or low resource usage matters.



Beyond the fact that it’s Clojure, Babashka brings a few features that make it stand apart from contenders:

First-class support for multi-threaded programming. Clojure makes multi-threaded programming simple and easy to write and reason about. With Babashka, you can write straightforward scripts that e.g. fetch and process data from multiple databases in parallel.

Real testing. You can unit test your Babashka code just as you would any other Clojure project. How do you even test bash?

Real project organization. Clojure namespaces are a sane way to organize your project’s functions and build reusable libraries.

Cross-platform compatibility. It’s nice not having to worry that an OS X-developed script is broken in your continuous integration pipeline.

Interactive Development. Following the lisp tradition, Babashka provides a read-eval-print loop (REPL) that gives you that good good bottom-up fast-feedback feeling. Script development is inherently a fast; Babashka makes it a faster.

Built-in tools for defining your script’s interface. One reason to write a shell script is to provide a concise, understandable interface for a complicated process. For example, you might write a build script that includes build and deploy commands that you call like



Babashka comes with tools that gives you a consistent way of defining such commands, and for parsing command-line arguments into Clojure data structures. Take that, bash!

A rich set of libraries. Babashka comes with helper utilities for doing typical shell script grunt work like interacting with processes or mucking about with the filesystem. It also has support for the following without needing extra dependencies:

    JSON parsing

    YAML parsing

    Starting an HTTP server

    Writing generative tests

And of course, you can add Clojure libraries as dependencies to accomplish even more. Clojure is a gateway drug to other programming paradigms, so if you ever wanted to do e.g. logic programming from the command line, now’s your chance!

Good error messages. Babashka’s error handling is the friendliest of all Clojure implementations, directing you precisely to where an error occurred.


Babashka Babooka: Write Command-Line Clojure
Introduction

    Buy the PDF and epub on gumroad

There are two types of programmers in the world: the practical, sensible, shell-resigned people who need to google the correct argument order for ln -s; and those twisted, Stockholmed souls who will gleefully run their company’s entire infrastructure on 57 commands stitched together into a single-line bash script.

This guide is for the former. For the latter: sorry, but I can’t help you.

Babashka is a Clojure scripting runtime that is a powerful, delightful alternative to the shell scripts you’re used to. This comprehensive tutorial will teach you:

    What babashka is, what it does, how it works, and how it can fit into your workflow

    How to write babashka scripts

    How to organize your babashka projects

    What pods are, and how they provide a native Clojure interface for external programs

    How to use tasks to create interfaces similar to make or npm

If you’d like to stop doing something that hurts (writing incomprehensible shell scripts) and start doing something that feels great (writing Babashka scripts), then read on!
Note
	If you’re unfamiliar with Clojure, Babashka is actually a great tool for learning! This crash course and this chapter on namespaces cover what you need to understand the Clojure used here. There are many good editor extensions for working with Clojure code, including Calva for VS Code and CIDER for emacs. If you’re new to the command line, check out Learn Enough Command Line to be Dangerous.
Sponsor

If you enjoy this tutorial, consider sponsoring me, Daniel Higginbotham, through GitHub sponsors. As of April 2022 I am spending two days a week working on free Clojure educational materials and open source libraries to make Clojure more beginner-friendly, and appreciate any support!

Please also consider sponsoring Michiel Borkent, aka borkdude, who created babashka. Michiel is doing truly incredible work to transform the Clojure landscape, extending its usefulness and reach in ways that benefit us all. He has a proven track record of delivering useful tools and engaging with the commuity.
What is Babashka?

From a user perspective, babashka is a scripting runtime for the Clojure programming language. It lets you execute Clojure programs in contexts where you’d typically use bash, ruby, python, and the like. Use cases include build scripts, command line utilities, small web applications, git hooks, AWS Lambda functions, and everywhere you want to use Clojure where fast startup and/or low resource usage matters.

You can run something like the following in a terminal to immediately execute your Clojure program:

bb my-clojure-program.clj

If you’re familiar with Clojure, you’ll find this significant because it eliminates the startup time you’d otherwise have to contend with for a JVM-compiled Clojure program, not to mention you don’t have to compile the file. It also uses much less memory than running a jar. Babashka makes it feasible to use Clojure even more than you already do.

If you’re unfamiliar with Clojure, using Babashka is a great way to try out the language. Clojure is a hosted language, meaning that the language is defined independently of the underlying runtime environment. Most Clojure programs are compiled to run on the Java Virtual Machine (JVM) so that they can be run anywhere Java runs. The other main target is JavaScript, allowing Clojure to run in a browser. With Babashka, you can now run Clojure programs where you’d normally run bash scripts. The time you spend investing in Clojure pays dividends as your knowledge transfers to these varied environments.

From an implementation perspective, Babashka is a standalone, natively-compiled binary, meaning that the operating system executes it directly, rather than running in a JVM. When the babashka binary gets compiled, it includes many Clojure namespaces and libraries so that they are usable with native performance. You can check out the full list of built-in namespaces. Babashka can also include other libraries, just like if you’re using deps.edn or Leiningen.

The binary also includes the Small Clojure Interpreter (SCI) to interpret the Clojure you write and additional libraries you include on the fly. Its implementation of Clojure is nearly at parity with JVM Clojure, and it improves daily thanks to Michiel Borkent's ceaseless work. It’s built with GraalVM. This guide is focused on becoming productive with Babashka and doesn’t cover the implementation in depth, but you can learn more about it by reading this article on the GraalVM blog.
Why should you use it?

I won’t go into the benefits of Clojure itself because there are plenty of materials on that elsewhere.

Beyond the fact that it’s Clojure, Babashka brings a few features that make it stand apart from contenders:

First-class support for multi-threaded programming. Clojure makes multi-threaded programming simple and easy to write and reason about. With Babashka, you can write straightforward scripts that e.g. fetch and process data from multiple databases in parallel.

Real testing. You can unit test your Babashka code just as you would any other Clojure project. How do you even test bash?

Real project organization. Clojure namespaces are a sane way to organize your project’s functions and build reusable libraries.

Cross-platform compatibility. It’s nice not having to worry that an OS X-developed script is broken in your continuous integration pipeline.

Interactive Development. Following the lisp tradition, Babashka provides a read-eval-print loop (REPL) that gives you that good good bottom-up fast-feedback feeling. Script development is inherently a fast; Babashka makes it a faster.

Built-in tools for defining your script’s interface. One reason to write a shell script is to provide a concise, understandable interface for a complicated process. For example, you might write a build script that includes build and deploy commands that you call like

./my-script build
./my-script deploy

Babashka comes with tools that gives you a consistent way of defining such commands, and for parsing command-line arguments into Clojure data structures. Take that, bash!

A rich set of libraries. Babashka comes with helper utilities for doing typical shell script grunt work like interacting with processes or mucking about with the filesystem. It also has support for the following without needing extra dependencies:

    JSON parsing

    YAML parsing

    Starting an HTTP server

    Writing generative tests

And of course, you can add Clojure libraries as dependencies to accomplish even more. Clojure is a gateway drug to other programming paradigms, so if you ever wanted to do e.g. logic programming from the command line, now’s your chance!

Good error messages. Babashka’s error handling is the friendliest of all Clojure implementations, directing you precisely to where an error occurred.
Installation

Installing with brew is brew install borkdude/brew/babashka
