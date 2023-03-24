## Быстрый поиск изоморфных подграфов


В данном репозитории размещен алгоритм быстрого поиска подграфов изоморфных
заданному паттерну с помощью минимального отсечения. Более подробное описание можно прочитать 
[на хабре](https://habr.com/ru/post/723328/)

Интерфейс метода, реализующий алгоритм быстрого поиска следующий:

```java
  public static List<CombinatoricsData> FastPatternResolver.getAllPatterns(int[][] source, int[][] pattern, boolean hardCheck)
```

Этот метод возвращает список объектов класса `CombinatoricsData`. Этот класс представляет
собой обёртку над целочисленным массивом. Для реализации быстрого поиска 
она не обязательна, однако с её помощью проще реализовывать логику.
Это вызвано тем, что в `java` для массивов не переопределены методы `.equals()` и `.hashCode()`.

Метод возвращается список обёрнутых массивов, каждый из которых 
хранит последовательность индексов. Если по этой последовательности построить подматрицу (будем называть 
её **переставленной подматрицей**), тогда
граф построенный из предположения, что полученная матрица является матрицей связности, будет 
изоморфным графу, построенному по матрице связности графа-паттерна. 

В качестве аргументов метод принимает матрицу связности исходного графа `source`, матрицу связности 
искомого паттерна `pattern` и флаг жёсткой сверки `hardCheck`. 
Если `hardCheck` равен `false`, то нулевому элементу паттерна
может соответствовать произвольное значение в переставленной подматрице, а если 
`true`, то все элементы паттерна и переставленной подматрицы должны
совпадать с точностью до перестановки. 

## Maven

Библиотека собрана в `maven`- зависимость. Пример подключения лежит [здесь](https://github.com/aok-buran/JavaPatternsDemo).

Чтобы подключить её, нужно добавить репозиторий в файл `pom.xml`: 

```xml
   <repositories>
        <repository>
            <id>buran-center</id>
            <url>https://mvn.buran.center/releases</url>
        </repository>
    </repositories>
```

Также необходимо подключить саму зависимость:

```xml
    <dependencies>
        <dependency>
            <groupId>center.buran.fast</groupId>
            <artifactId>patterns</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```
