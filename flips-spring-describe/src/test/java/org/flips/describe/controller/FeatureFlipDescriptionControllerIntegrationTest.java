package org.flips.describe.controller;

import org.flips.describe.TestFeatureFlipDescriptionContextConfiguration;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestFeatureFlipDescriptionContextConfiguration.class)
@WebAppConfiguration
public class FeatureFlipDescriptionControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp(){
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void shouldReturnAllFeatureDescriptionGivenFeaturesWereCached() throws Exception {
       mvc.perform(get("/describe/features"))
           .andExpect(jsonPath("$", Matchers.hasSize(3)))
           .andExpect(jsonPath("$[0].feature", Matchers.equalTo("feature1")))
           .andExpect(jsonPath("$[0].class",   Matchers.equalTo("org.flips.describe.fixture.TestClientFeatureFlipAnnotationsDescription")))
           .andExpect(jsonPath("$[0].enabled", Matchers.equalTo(true)))
           .andExpect(jsonPath("$[1].feature", Matchers.equalTo("feature2")))
           .andExpect(jsonPath("$[1].class",   Matchers.equalTo("org.flips.describe.fixture.TestClientFeatureFlipAnnotationsDescription")))
           .andExpect(jsonPath("$[1].enabled", Matchers.equalTo(false)))
           .andExpect(jsonPath("$[2].feature", Matchers.equalTo("feature3")))
           .andExpect(jsonPath("$[2].class",   Matchers.equalTo("org.flips.describe.fixture.TestClientFeatureFlipAnnotationsDescription")))
           .andExpect(jsonPath("$[2].enabled", Matchers.equalTo(true)));
    }
}