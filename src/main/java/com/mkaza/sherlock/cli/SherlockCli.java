package com.mkaza.sherlock.cli;

import com.mkaza.sherlock.api.Sherlock;
import com.mkaza.sherlock.api.SherlockConfig;
import com.mkaza.sherlock.api.TestCaseSherlock;
import com.mkaza.sherlock.clusterer.ClustererConfig;
import com.mkaza.sherlock.model.TestCaseCluster;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class SherlockCli {

    private static final Logger logger = Logger.getLogger(SherlockCli.class);

    private static final String LOGS_PATH_OPT = "lp";
    private static final String EPSILON_OPT = "e";
    private static final String MIN_PTS_OPT = "mp";

    public static void main(String[] args) {
        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);

            String path = null;
            if (cmd.hasOption(LOGS_PATH_OPT)) {
                path = cmd.getOptionValue(LOGS_PATH_OPT);
                logger.info("Path to logs: " + path);
            }

            SherlockConfig sherlockConfig;
            if (cmd.hasOption(EPSILON_OPT) || cmd.hasOption(MIN_PTS_OPT)) {
                String epsilon = cmd.getOptionValue(EPSILON_OPT);
                String minPts = cmd.getOptionValue("mp");
                logger.info("Optional parameters: epsilon: " + epsilon + "min points: " + minPts);
                sherlockConfig = SherlockConfig.builder(path)
                        .clustererConfig(
                                ClustererConfig.builder()
                                        .epsilon(Double.valueOf(epsilon))
                                        .minPts(Integer.valueOf(minPts))
                                        .build())
                        .build();
            } else {
                sherlockConfig = SherlockConfig.builder(path).build();
            }

            Sherlock<TestCaseCluster> sherlock = new TestCaseSherlock(sherlockConfig);
            List<TestCaseCluster> clusters = sherlock.cluster();
            clusters.forEach(
                    c -> logger.info(
                            "Cluster #" + clusters.indexOf(c) + " size: " + c.getCases().size() + StringUtils.LF +
                                    c.getCases().stream().map(p -> "\tTest name: " + p.getTestName()).collect(Collectors.joining(",\n")))
            );

        } catch (ParseException pe) {
            logger.error("Error parsing command-line arguments!");
            logger.error("Please, follow the instructions below:");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "CLI options for clustering", options );
            System.exit(1);
        }
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption(Option.builder(LOGS_PATH_OPT)
                .longOpt("logs path")
                .hasArg(true)
                .desc("[REQUIRED] path to test logs")
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
