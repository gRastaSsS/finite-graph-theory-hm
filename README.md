# Теория конечных графов и ее приложения. Практическая работа

**Сторонние библиотеки:**
- https://gephi.org/toolkit/ - импорт .gexf файлов;
- http://fastutil.di.unimi.it/ - расширенная библиотека структур данных;
- http://la4j.org/ - нахождение собственных векторов матрицы и перевод матрицы в .csv формат;
- http://graphstream-project.org/ - визуализация графов:
- https://knowm.org/open-source/xchart/ - построение гистограммы.

**Исходный код программы (Java): **
https://github.com/gRastaSsS/finite-graph-theory-hm

**Класс графа - FastGraph.**
**Все алгоритмы над графами реализованы в классе GraphUtil:**
- stronglyConnectedComponents - нахождение компонент сильной связности через DFS;
- weaklyConnectedComponents - нахождение компонент слабой связности через DFS;
- shortestPaths - нахождение длин кратчайших путей между всеми парами вершин в графе алгоритмом Флойда - Уоршелла;
- findAllShortestPaths - нахождение всех кратчайших путей между всеми парами вершин в графе;
- commonNeighborsIndex, jaccardsIndex, adamicAdarIndex, preferentialAttachmentIndex - меры сходства узлов графа;
- eigenVectorCentrality, degreeCentrality, closenessCentrality, betweennessCentrality - метрики центральности вершин;
- edgeBetweennessCentrality - метрика ребер.
