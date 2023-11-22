The Emerald Programming Language
URL  https://www.emeraldprogramminglanguage.org/

The Emerald Programming Language
This website documents the history of the Emerald programming language.

Emerald is a distributed, object-oriented programming language that was developed at the University of Washington starting in 1984. The goal of Emerald was to simplify the construction of distributed applications. This goal was reflected at every level of the system: its object structure, the programming language design, the compiler implementation, and the run-time support.

A short Emerald program (affectionately known as “Kilroy was here”) is shown on the right.

The paper describing Emerald which appears in the proceedings of Third ACN SIGPLAN History of Programming Language Conference. (pdf)
This is the authors' version of the work. It is posted here by permission of ACM for your personal use. Not for redistribution. The definitive version was published in Proceedings of the third ACM SIGPLAN conference on History of programming languages 2007, San Diego, California, June 09 - 10, 2007, pp. 11-1–11-51. http://doi.acm.org/10.1145/1238844.1238855

	

const Kilroy ←  object Kilroy
  process
    const origin ←  locate self
    const up ← origin.getActiveNodes
    for e in up
      	const there ← e.getTheNode
      	move self to there
    end for
    move self to origin
  end process
end Kilroy

Historical Documents

    Eden Invocation Timings
        8 Sept 1983
            Page 1
            Page 2
            Page 3 
        24 Oct 1983
        Why is Eden Slow? (18 Nov 1983) 
    Getting to Oz (27 Apr 1984)
    Minutes (18 Mar 1985)
    The whiteboard in Norm and Eric's office
    Emerald Versions 1985 and 1986
    AbCon diagram from TSE paper
    An early discussion of object groups in Emerald
    Early Emerald Invocation timings
        February 21, 1986
        March 13, 1986
        March 18, 1986
        March 19, 1986 

Hysterical Documents

    A review of the 1987 SOSP paper by a UCB student 

Published Papers

    Object Structure in the Emerald System, Oopsla 1986.
    Fine-Grained Mobility in the Emerald System, Symposium on Operating System Principles, 1987 (Extended Abstract).
    Fine-Grained Mobility in the Emerald System, Transactions on Computer Systems, 1988.
    Emerald: A General Purpose Programming Language, Transactions on Software Engineering, 1991.
    A Compositional Model for Software Reuse, The Computer Journal, 1989. 

Non published papers

    The Lattice of Data Type, or Much Ado about NIL, somewhere between 1987 and 1991.
    The Emerald System User's Guide, University of Washington, November, 1988.
    An Efficient Implementation of Distributed Object Persistence, July 1989.
    Typechecking Polymorphism in Emerald, December 1990.
    The Emerald Programming Language Report, 1991. 

Ph.D. Theses

    Eric Jul's thesis
    Norm Hutchinson's thesis
    Niels Christian Juul's thesis 

Current (more-or-less) Emerald information
Emerald on Source Forge
Emerald at UBC 
