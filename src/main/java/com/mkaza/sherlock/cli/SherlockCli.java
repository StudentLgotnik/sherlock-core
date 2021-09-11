package com.mkaza.sherlock.cli;

import java.util.List;
import java.util.stream.Collectors;

import com.mkaza.sherlock.api.Sherlock;
import com.mkaza.sherlock.api.SherlockConfig;
import com.mkaza.sherlock.api.TestCaseSherlock;
import com.mkaza.sherlock.clusterer.ClustererConfig;
import com.mkaza.sherlock.model.TestCaseCluster;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

public class SherlockCli {

    private static final String FILE_PATH_OPT = "f";
    private static final String EPSILON_OPT = "e";
    private static final String MIN_PTS_OPT = "mp";

    public static void main(String[] args) {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);

            String file = null;
            if (cmd.hasOption(FILE_PATH_OPT)) {
                file = cmd.getOptionValue(FILE_PATH_OPT);
                System.out.println("Path to file: " + file);
            }

            SherlockConfig sherlockConfig;
            if (cmd.hasOption(EPSILON_OPT) || cmd.hasOption(MIN_PTS_OPT)) {
                String epsilon = cmd.getOptionValue(EPSILON_OPT);
                String minPts = cmd.getOptionValue("mp");
                System.out.println("Optional parameters: epsilon: " + epsilon + "min points: " + minPts);
                sherlockConfig = SherlockConfig.builder(file)
                        .clustererConfig(
                                ClustererConfig.builder()
                                        .epsilon(Double.valueOf(epsilon))
                                        .minPts(Integer.valueOf(minPts))
                                        .build())
                        .build();
            } else {
                sherlockConfig = SherlockConfig.builder(file).build();
            }

            Sherlock<TestCaseCluster> sherlock = new TestCaseSherlock(sherlockConfig);
            List<TestCaseCluster> clusters = sherlock.cluster();
            clusters.forEach(
                    c -> System.out.println(
                            "Cluster #" + clusters.indexOf(c) + " size: " + c.getCases().size() + StringUtils.LF +
                                    c.getCases().stream().map(p -> "\tTest name: " + p.getTestName()).collect(Collectors.joining(",\n")))
            );

        } catch (ParseException pe) {
            System.out.println("Error parsing command-line arguments!");
            System.out.println("Please, follow the instructions below:");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "CLI options for clustering", options );
            System.exit(1);
        }
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption(Option.builder(FILE_PATH_OPT)
                .longOpt("file path")
                .hasArg(true)
                .desc("[REQUIRED] path to one test log file")
                .required()
                .build());
        options.addOption(Option.builder(EPSILON_OPT)
                .longOpt("epsilon")
                .hasArg(true)
                .desc("The maximum distance between two clustering samples as double")
                .required(false)
                .build());
        options.addOption(Option.builder(MIN_PTS_OPT)
                .longOpt("minPts")
                .hasArg(true)
                .desc("The number as integer of samples in a neighborhood for a point to be considered as a core point")
                .required(false)
                .build());

        return options;
    }
}
