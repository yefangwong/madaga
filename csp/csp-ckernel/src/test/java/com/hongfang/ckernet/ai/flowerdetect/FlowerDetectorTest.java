package com.hongfang.ckernet.ai.flowerdetect;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class FlowerDetectorTest {
	
	@InjectMocks
	FlowerDetector detector = new FlowerDetector();
	
	Flower flower;
	
	@Mock
	FlowerCategory category;
	
	@Before
	public void init() {
		flower = new Flower(null);
	}

	@Test
	public void testDetect() throws Exception {
		FlowerCategory category = detector.detect(flower);
		assertEquals(FlowerCategory.Balsamina, category.name());
	}

}
