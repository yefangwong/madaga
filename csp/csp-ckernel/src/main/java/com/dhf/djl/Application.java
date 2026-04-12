/*
 * Copyright 2023 yefangwong(https://github.com/yefangwong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dhf.djl;

import java.io.IOException;
import java.nio.file.*;

import ai.djl.*;
import ai.djl.nn.*;
import ai.djl.nn.core.*;
import ai.djl.basicdataset.cv.classification.Mnist;
import ai.djl.ndarray.types.*;
import ai.djl.training.*;
import ai.djl.training.dataset.*;
import ai.djl.training.initializer.*;
import ai.djl.training.loss.*;
import ai.djl.training.listener.*;
import ai.djl.training.evaluator.*;
import ai.djl.training.optimizer.*;
import ai.djl.training.util.*;
import ai.djl.basicmodelzoo.cv.classification.*;
import ai.djl.basicmodelzoo.basic.*;
import ai.djl.translate.TranslateException;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：Application.java<br>
 * 描             述 ：<br>
 * 公             司 ：DeHongFang Technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * 2023/10/10 init for JavaDL library import Mark
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2023/10/10<P>
 */
public class Application {
    public static void main(String[] args) throws IOException, TranslateException {
        ai.djl.Application application = ai.djl.Application.CV.IMAGE_CLASSIFICATION;

        long inputSize = 28L*28L;
        long outputSize = 10L;

        SequentialBlock block = new SequentialBlock();
        block.add(Blocks.batchFlattenBlock(inputSize));
        block.add(Linear.builder().setUnits(128).build());
        block.add(Activation::relu);
        block.add(Linear.builder().setUnits(64).build());
        block.add(Activation::relu);
        block.add(Linear.builder().setUnits(outputSize).build());

        int batchSize = 32;
        Mnist mnist = Mnist.builder().setSampling(batchSize, true).build();
        mnist.prepare(new ProgressBar());
//TODO : ls /usr/local/lib/libmxnet.dylib to /Users/yefangwong/.djl.ai/mxnet/1.9.1-mkl-osx-aarch64/libmxnet.dylib
        Model model = Model.newInstance("mlp");
        model.setBlock(new Mlp(28 * 28, 10, new int[] {128, 64}));
//
        DefaultTrainingConfig config = new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
//                //softmaxCrossEntropyLoss is a standard loss for classification problems
                .addEvaluator(new Accuracy()) // Use accuracy so we humans can understand how accurate the model is
                .addTrainingListeners(TrainingListener.Defaults.logging());
//
//        // Now that we have our training configuration, we should create a new trainer for our model
        Trainer trainer = model.newTrainer(config);
        trainer.initialize(new Shape(1, 28 * 28));
//
        // Deep learning is typically trained in epochs where each epoch trains the model on each item in the dataset once.
        int epoch = 2;
//
        EasyTrain.fit(trainer, epoch, mnist, null);
//
        Path modelDir = Paths.get("build/mlp");
        Files.createDirectories(modelDir);
//
        model.setProperty("Epoch", String.valueOf(epoch));
//
        model.save(modelDir, "mlp");
    }
}