package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.TreeMap;

@Data
@AllArgsConstructor
public class TestClass {
    private int count;
    private TreeMap<String, String> users;

}
