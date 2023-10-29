package com.hackerrank.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.api.repository.FlightRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ApplicationTest {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final ObjectMapper om = new ObjectMapper();
    @Autowired
    FlightRepository flightRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        flightRepository.deleteAll();
    }

    @Test
    public void testFlightEndpointWithPOST() throws Exception {
        TestModel expectedRecord = getTestData().get("F1");
        TestModel actualRecord = om.readValue(mockMvc.perform(post("/flight")
                .contentType("application/json")
                .content(om.writeValueAsString(getTestData().get("F1"))))
                .andDo(print())
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), TestModel.class);

        Assertions.assertTrue(new ReflectionEquals(expectedRecord, "id").matches(actualRecord));
        assertEquals(true, flightRepository.findById(actualRecord.getId()).isPresent());
    }

    @Test
    public void testFlightEndpointWithGETList() throws Exception {
        Map<String, TestModel> data = getTestData();
        data.remove("F2");
        List<TestModel> expectedRecords = new ArrayList<>();

        for (Map.Entry<String, TestModel> kv : data.entrySet()) {
            expectedRecords.add(om.readValue(mockMvc.perform(post("/flight")
                    .contentType("application/json")
                    .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), TestModel.class));
        }
        Collections.sort(expectedRecords, Comparator.comparing(TestModel::getId));

        List<TestModel> actualRecords = om.readValue(mockMvc.perform(get("/flight"))
                .andDo(print())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(expectedRecords.size())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<TestModel>>() {
        });

        for (int i = 0; i < expectedRecords.size(); i++) {
            Assertions.assertTrue(new ReflectionEquals(expectedRecords.get(i)).matches(actualRecords.get(i)));
        }
    }

    @Test
    public void testFlightEndpointWithGETListAndOriginFilter() throws Exception {
        String origin = "KTM";
        Map<String, TestModel> data = getTestData();
        List<TestModel> expectedRecords = new ArrayList<>();

        for (Map.Entry<String, TestModel> kv : data.entrySet()) {
            expectedRecords.add(om.readValue(mockMvc.perform(post("/flight")
                    .contentType("application/json")
                    .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), TestModel.class));
        }
        expectedRecords = expectedRecords.stream().filter(r -> r.getOrigin().equals(origin)).collect(Collectors.toList());

        List<TestModel> actualRecords = om.readValue(mockMvc.perform(get("/flight?origin=" + origin))
                .andDo(print())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(expectedRecords.size())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<TestModel>>() {
        });

        for (int i = 0; i < expectedRecords.size(); i++) {
            Assertions.assertTrue(new ReflectionEquals(expectedRecords.get(i)).matches(actualRecords.get(i)));
        }

        mockMvc.perform(get("/flight?origin=None"))
                .andDo(print())
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    public void testFlightEndpointWithGETListAndDestinationOrder() throws Exception {
        List<TestModel> expectedRecords = new ArrayList<>();

        for (Map.Entry<String, TestModel> kv : getTestData().entrySet()) {
            expectedRecords.add(om.readValue(mockMvc.perform(post("/flight")
                    .contentType("application/json")
                    .content(om.writeValueAsString(kv.getValue())))
                    .andDo(print())
                    .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), TestModel.class));
        }
        Collections.sort(expectedRecords, Comparator.comparing(TestModel::getDestination).thenComparing(TestModel::getId));

        List<TestModel> actualRecords = om.readValue(mockMvc.perform(get("/flight?orderBy=destination"))
                .andDo(print())
                .andExpect(jsonPath("$", isA(ArrayList.class)))
                .andExpect(jsonPath("$", hasSize(expectedRecords.size())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<TestModel>>() {
        });

        for (int i = 0; i < expectedRecords.size(); i++) {
            Assertions.assertTrue(new ReflectionEquals(expectedRecords.get(i)).matches(actualRecords.get(i)));
        }

        Collections.sort(expectedRecords, Comparator.comparing(TestModel::getDestination, Comparator.reverseOrder()).thenComparing(TestModel::getId));

        actualRecords = om.readValue(mockMvc.perform(get("/flight?orderBy=-destination"))
                .andDo(print())
                .andExpect(jsonPath("$", isA(ArrayList.class)))
                .andExpect(jsonPath("$", hasSize(expectedRecords.size())))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), new TypeReference<List<TestModel>>() {
        });

        for (int i = 0; i < expectedRecords.size(); i++) {
            Assertions.assertTrue(new ReflectionEquals(expectedRecords.get(i)).matches(actualRecords.get(i)));
        }
    }

    @Test
    public void testFlightEndpointWithGETById() throws Exception {
        TestModel expectedRecord = om.readValue(mockMvc.perform(post("/flight")
                .contentType("application/json")
                .content(om.writeValueAsString(getTestData().get("F2"))))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), TestModel.class);

        TestModel actualRecord = om.readValue(mockMvc.perform(get("/flight/" + expectedRecord.getId())
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString(), TestModel.class);

        Assertions.assertTrue(new ReflectionEquals(expectedRecord).matches(actualRecord));

        mockMvc.perform(get("/flight/" + Integer.MAX_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Map<String, TestModel> getTestData() {
        Map<String, TestModel> data = new LinkedHashMap<>();

        TestModel f1 = new TestModel(
                "F1",
                "KTM",
                "DHL",
                Arrays.asList(240, 215, 240, 195, 255, 255, 240));
        data.put("F1", f1);

        TestModel f2 = new TestModel(
                "F2",
                "CKL",
                "UML",
                Arrays.asList(240, 360, 285, 290, 320));
        data.put("F2", f2);

        TestModel f3 = new TestModel(
                "F3",
                "CKL",
                "UML",
                Arrays.asList(950, 805, 800, 800, 900, 605));
        data.put("F3", f3);

        return data;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class TestModel {
        protected Integer id;
        protected String flight;
        protected String origin;
        protected String destination;
        protected List<Integer> speedSeries;

        public TestModel(String flight, String origin, String destination, List<Integer> speedSeries) {
            this.flight = flight;
            this.origin = origin;
            this.destination = destination;
            this.speedSeries = speedSeries;
        }
    }
}
