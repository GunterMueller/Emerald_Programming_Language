# `vmc` Tests

The role of `vmc` in the Emerald compiler is at present unclear, hence
its intended behaviour is also _unclear_.

The purpose of this test-suite is therefore to make a snapshot of the
_current_ behaviour of `vmc`, right or wrong, before any subsequent
changes are made to the code base (e.g., to bring `vmc` up-to-date
with contemporary C development standards).

## Design

`vmc` is a compiler that generates C code.

For an input `.desc` file, it generates 3 C files, ending in `desc.c`,
`desc.h`, and `desc_i.h`, respectively. For instance, for the empty
file [`data/empty.desc`](data/empty.desc), `vmc` generates
[`data/empty.desc.c`](data/empty.desc.c),
[`data/empty.desc.h`](data/empty.desc.h), and
[`data/empty.desc_i.h`](data/empty.desc_i.h).

The [(tests) `data`](data) directory is therefore structured as
follows:

1. The code generated for the empty file
   [`data/empty.desc`](data/empty.desc) is stored directly in Git.
2. For all other sample inputs, only the input file and a _patch_ are
   stored in Git.
   1. The patch is generated by [`gen-patch.sh`](gen-patch.sh).
   2. The code generated for a given input file can be restored from
      the patch using [`gen-data.sh`](gen-data.sh).

      For instance, to restore `data/empty_defs.desc.c`,
      `data/empty_defs.desc.h`, and `data/empty_defs.desc_i.h`, which were
      generated for [`data/empty_defs.desc`](data/empty_defs.desc):

      ~~~
      $ bash gen-data.sh data/empty_defs.patch
      ~~~

This patch-based design reduces code duplication, and enables a
principled exploration of the behaviour of `vmc` for various inputs.