# Теория конечных графов и ее приложения. Практическая работа

**Исходный (сгенерированный) граф:** 

https://github.com/gRastaSsS/finite-graph-theory-hm/blob/master/src/main/resources/getaway/vk-friends.gexf

**Сторонние библиотеки:**
- https://gephi.org/toolkit/ - импорт .gexf файлов;
- http://fastutil.di.unimi.it/ - расширенная библиотека структур данных;
- http://la4j.org/ - нахождение собственных векторов матрицы и перевод матрицы в .csv формат;
- http://graphstream-project.org/ - визуализация графов:
- https://knowm.org/open-source/xchart/ - построение гистограммы.

**Main-класс - [Main.java](https://github.com/gRastaSsS/finite-graph-theory-hm/blob/master/src/main/java/candlelight/Main.java)**

**Класс графа - [FastGraph.java](https://github.com/gRastaSsS/finite-graph-theory-hm/blob/master/src/main/java/candlelight/model/FastGraph.java).**

**Класс визуализации графа (с заданными метриками) - [GraphViewer.java](https://github.com/gRastaSsS/finite-graph-theory-hm/blob/master/src/main/java/candlelight/GraphViewer.java).**

**Класс визуализации гистограммы - [ChartUtil.java](https://github.com/gRastaSsS/finite-graph-theory-hm/blob/master/src/main/java/candlelight/ChartUtil.java)**

**Класс конвертации графа в матрицу смежности - [Converter.java](https://github.com/gRastaSsS/finite-graph-theory-hm/blob/master/src/main/java/candlelight/Converter.java).**

**Все алгоритмы над графами реализованы в классе [GraphUtil.java](https://github.com/gRastaSsS/finite-graph-theory-hm/blob/master/src/main/java/candlelight/GraphUtil.java):**
- stronglyConnectedComponents - нахождение компонент сильной связности через DFS;
- weaklyConnectedComponents - нахождение компонент слабой связности через DFS;
- shortestPaths - нахождение длин кратчайших путей между всеми парами вершин в графе алгоритмом Флойда - Уоршелла;
- findAllShortestPaths - нахождение всех кратчайших путей между всеми парами вершин в графе;
- commonNeighborsIndex, jaccardsIndex, adamicAdarIndex, preferentialAttachmentIndex - меры сходства узлов графа;
- eigenVectorCentrality, degreeCentrality, closenessCentrality, betweennessCentrality - метрики центральности вершин;
- edgeBetweennessCentrality - метрика ребер.
