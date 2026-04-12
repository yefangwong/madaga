package com.dhf.djl;
import java.io.IOException;
import java.nio.file.*;

import ai.djl.*;
import ai.djl.modality.*;
import ai.djl.modality.cv.*;
import ai.djl.modality.cv.transform.*;
import ai.djl.modality.cv.translator.*;
import ai.djl.repository.zoo.*;
import ai.djl.translate.*;
import ai.djl.training.util.*;
import lombok.var;

public class ImageClassifier {
    public static void main(String[] args) throws IOException, ModelNotFoundException, MalformedModelException, TranslateException {
        DownloadUtils.download("https://djl-ai.s3.amazonaws.com/mlrepo/model/cv/image_classification/ai/djl/pytorch/resnet/0.0.1/traced_resnet18.pt.gz", "build/pytorch_models/resnet18/resnet18.pt", new ProgressBar());
        DownloadUtils.download("https://djl-ai.s3.amazonaws.com/mlrepo/model/cv/image_classification/ai/djl/pytorch/synset.txt", "build/pytorch_models/resnet18/synset.txt", new ProgressBar());

        float[] mean = {0.485f, 0.456f, 0.406f};
        float[] std = {0.229f, 0.224f, 0.225f};

        Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
                .addTransform(new Resize(256))
                .addTransform(new CenterCrop(224, 224))
                .addTransform(new ToTensor())
                .addTransform(new Normalize(
                        new float[] {0.485f, 0.456f, 0.406f},
                        new float[] {0.229f, 0.224f, 0.225f}))
                .optApplySoftmax(true)
                .build();

        Criteria<Image, Classifications> criteria = Criteria.builder()
                .setTypes(Image.class, Classifications.class)
                .optModelPath(Paths.get("build/pytorch_models/resnet18"))
                .optModelName("resnet18.pt")
                .optEngine("PyTorch")
                .optOption("mapLocation", "true") // this model requires mapLocation for GPU
                .optTranslator(translator)
                .optProgress(new ProgressBar()).build();

        ZooModel<Image, Classifications> model = criteria.loadModel();

        var img = ImageFactory.getInstance().fromUrl("https://raw.githubusercontent.com/pytorch/hub/master/images/dog.jpg");
        img.getWrappedImage();

        var predictor = model.newPredictor(translator);
        var classifications = predictor.predict(img);
        System.out.println(classifications);
    }
}
