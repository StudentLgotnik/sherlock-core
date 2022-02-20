<!-- ABOUT THE PROJECT -->
## About The Project
In today's world, any software development is not without testing. 
And this process requires a large amount of human resources, 
so information services that will help improve the quality and speed of such services are quite important.

The aim of the project is to cluster similar failed tests to ensure the effectiveness of solving the analysis problem based on the study of errors, 
the structure of reference data and the introduction of this knowledge into the classification process. 
And also free testers from the lengthy process of analyzing program logs in order to focus them on correcting the cause itself.

### What problem it solves
Every major product(software) has in its arsenal automated integration testing. 
And for the development and maintenance of automated testing is usually allocated a separate team. 
And after a bug in a particular module of the program usually crashes a large number of tests that use this module. 
And team members begin to review the test log to identify the problem. 
And if several engineers are reviewing different tests, and the reason is the same, 
then the analysis time of one engineer was wasted, because the problem is identical in both tests. 
And the opposite problem can arise when the results are reviewed by one engineer, 
and he believes that all the tests failed for one reason, and the problems are actually two. 
And it would be good to have software that could classify the actual number of errors, and indicate to which test which error refers.

### Targeted users
Large software products that have automated integrated testing with a significant number of tests. 
Directly to teams that develop and maintain automated integration testing or to teams that do not have a separate team but want to have an error classifier.


<!-- GETTING STARTED -->
## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites
Install:
* [JDK 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
* [Maven](https://maven.apache.org/install.html)

### Build

Below is an example of how you can build the project.

```shell
mvn clean install
```


<!-- USAGE EXAMPLES -->
## Usage

Below is an example of how you can use the project.

### Command line

1. Build the project
2. Go to the folder with the assembled jar `cd target`
3. Run command `java -jar sherlock-core-1.0.0.jar -lp <path_to_logs>`(replace `<path_to_logs>` with your path to single file or folder with logs)

Command example with test data: 

`java -jar sherlock-core-1.0.0.jar -lp ../src/test/resources/surefire/reports/` 

### API Samples

Before usin API go to [artifactory](https://mkaza.jfrog.io/ui/repos/tree/General/sherlock%2Fcom%2Fmkaza%2Fsherlock%2Fsherlock-core%2F1.0.0%2Fsherlock-core-1.0.0.jar)
and add library as a dependency.

```java
import com.mkaza.sherlock.api.Sherlock;
import com.mkaza.sherlock.api.SherlockConfig;
import com.mkaza.sherlock.api.TestCaseSherlock;
import com.mkaza.sherlock.clusterer.impl.DBSCANConfig;
import com.mkaza.sherlock.model.TestCaseCluster;
import com.mkaza.sherlock.parser.impl.XmlLogParser;
import com.mkaza.sherlock.parser.provider.impl.DirLogsProvider;

import java.nio.file.Paths;
import java.util.List;

public class Sample {
    public static void main(String[] args) {

        var relPath = Paths.get("src", "test", "resources", "surefire", "reports");
        var testLogsPath = relPath.toFile().getAbsolutePath();

        //Setup sherlock configuration via builder
        SherlockConfig config = SherlockConfig
                //required logs provider to know how to receive your data or use default one(FileLogsProvider or DirLogsProvider)
                .builder(new DirLogsProvider(testLogsPath))
                //parser to determine how to parse your logs
                .parser(new XmlLogParser())
                //define advance clusterer configuration
                .clustererConfig(
                        DBSCANConfig.builder()
                                //search radius for neighboring points during clustering
                                .epsilon(5.642)
                                //the number of points required in a radius(epsilon) to assign a point to a cluster
                                .minPts(2)
                                .build()
                )
                .build();

        //Initialize sherlock clusterer based on configuration
        Sherlock<TestCaseCluster> sherlock = new TestCaseSherlock(config);

        //Execute sherlock clusterer to receive clusters
        List<TestCaseCluster> clusters = sherlock.cluster();
    }
}
```

<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/StudentLgotnik/sherlock-core/issues) for a full list of proposed features (and known issues). 

<!-- CONTRIBUTING -->
## Contributing

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.


<!-- CONTACT -->
## Contact

Email: kazzarin99@gmail.com

Project Link: [https://github.com/StudentLgotnik/sherlock-core](https://github.com/StudentLgotnik/sherlock-core)
