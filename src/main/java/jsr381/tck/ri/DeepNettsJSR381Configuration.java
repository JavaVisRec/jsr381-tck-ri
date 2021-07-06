package jsr381.tck.ri;

import jsr381.tck.spi.JSR381Configuration;

import javax.visrec.ImageFactory;
import javax.visrec.ml.classification.NeuralNetBinaryClassifier;
import javax.visrec.ml.classification.NeuralNetImageClassifier;
import javax.visrec.ri.BufferedImageFactory;
import javax.visrec.ri.spi.BufferedImageClassifierFactory;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class DeepNettsJSR381Configuration implements JSR381Configuration {
    @Override
    public NeuralNetImageClassifier.BuildingBlock<BufferedImage> getABImageClassificationBuildingBlock(NeuralNetImageClassifier.Builder<BufferedImage> builder) {
        return getABImageClassificationBuilder(builder).getBuildingBlock();
    }

    @Override
    public NeuralNetImageClassifier.Builder<BufferedImage> getABImageClassificationBuilder(NeuralNetImageClassifier.Builder<BufferedImage> builder) {
        URL directoryUrl = DeepNettsJSR381Configuration.class.getClassLoader().getResource("12-image-classifier");
        if (directoryUrl == null)
            throw new IllegalStateException("Unable to find 12-image-classifier directory in resources.");

        var directory = new File(directoryUrl.getFile()).getAbsolutePath();

        var tempOutputModel = Path.of("ab.dnet");
        var tempOutputFile = tempOutputModel.toFile();
        try {
            if (!tempOutputFile.exists() && tempOutputFile.createNewFile())
                tempOutputFile.deleteOnExit();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create temporary model file", e);
        }

        return builder
                .labelsFile(Paths.get(directory, "labels.txt"))
                .trainingFile(Paths.get(directory, "training_data/train.txt"))
                .networkArchitecture(Paths.get(directory, "arch.json"))
                .exportModel(tempOutputModel);
    }

    @Override
    public NeuralNetBinaryClassifier.BuildingBlock<float[]> getSpamBinaryClassificationBuildingBlock(NeuralNetBinaryClassifier.Builder<float[]> builder) {
        return getSpamBinaryClassificationBuilder(builder).getBuildingBlock();
    }

    @Override
    public NeuralNetBinaryClassifier.Builder<float[]> getSpamBinaryClassificationBuilder(NeuralNetBinaryClassifier.Builder<float[]> builder) {
        URL spamCsvResource = DeepNettsJSR381Configuration.class.getClassLoader().getResource("spam.csv");
        if (spamCsvResource == null)
            throw new IllegalStateException("Unable to find spam.csv in resources.");
        var trainingPath = Path.of(new File(spamCsvResource.getFile()).getAbsolutePath());
        return builder.trainingPath(trainingPath);
    }

    @Override
    public List<ImageFactory<?>> getImageFactories() {
        return Arrays.asList(new BufferedImageFactory());
    }
}
