## VDA ZAPROS (Verbal Decision Analysis)

### About

This repository is a module of the alternatives ranking system based on the methods of the ZAPROS family. The module provides 4 methods &ndash; ZAPROS II, ZAPROS III, ARACE and the experimental ARACE QV method which combines the features of the ARACE and ZAPROS III methods.

The project is completed during the preparation of [Daniil A. Antokhin](https://github.com/ADanielGhost) bachalor's thesis at SPbPU Institute of Computer Science and Technology (SPbPU ICST).

### Authors and contributors

The advisor and contributor [Vladimir A. Parkhomenko](https://github.com/ParkhomenkoV) assistant of SPbPU ICST. The main contributor [Daniil A. Antokhin](https://github.com/ADanielGhost) student of SPbPU ICST.

### Acknowledgment

The authors are grateful to SPbPU Institute of computer science and technology (SPbPU ICST). Special thanks are to Alexander V. Shchukin the head of Applied Informatics program for the support.

### Warranty

The contributors give no warranty for the using of the software.

### License

This program is open to use anywhere and is licensed under the [MIT license](./LICENSE).

### Usage

Use maven or another build automation tool for implement in your project. The current version can always be found [here](https://github.com/ADanielGhost/vda-zapros/packages).

Use class _VdaZaprosFactory_ on client side. This class has 2 static methods &ndash; _getService_ and _getConfig_.

The first one uses the _MethodType_ type parameter, which is responsible for the method we need and gives the output of the _VdaZaprosService_ instance implementation (ZAPROS II, ZAPROS III and another).

The second method takes a parameter of the _List\<Criteria\>_ type, which contains a set of criteria for which we build a config for building quasiexperts, which will later be used as a parameter to the _VdaZaprosService_ methods.

### Examples

In the example below, we get the _ARACE_ method and the config for the _criteriaList_ criteria set:

     VdaZaprosService service = VdaZaprosFactory.getService(MethodType.ARACE);
     QuasiExpertConfig config = VdaZaprosFactory.getConfig(criteriaList);

Also, the module has 2 implementations &ndash; console [console-zapros-impl](https://github.com/ADanielGhost/console-zapros-impl) and web implementation [zapros-web](https://github.com/ADanielGhost/zapros-web).