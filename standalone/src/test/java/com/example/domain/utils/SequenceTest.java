package com.example.domain.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

class SequenceTest {

    StringIdGenerator sequence;

    @BeforeEach
    void setUp() {
        sequence = new StringIdGenerator(0, 0);
    }

    @Test
    void getId() {
        long l = sequence.nextId();
        System.out.println(l);
        String s = Base64Utils.encodeToString(String.valueOf(l).getBytes());
        System.out.println(s);
    }

    @Test
    void files() {

        List<File> files = FileUtil.loopFiles("/dev/flowable-demo");
        List<String> paths = files.parallelStream().map(File::getPath).collect(Collectors.toList());
        paths.parallelStream().forEach(System.out::println);
    }

    @SneakyThrows
    @Test
    void command() {
        String[] getVersionCmd = {"ossutil"," cat oss://bring-resources/libs/test/static/oss.version"};
//        String syncCmd = "ossutil.exe sync /static/ oss://bring-resources/libs/test/static/ -f";


        String version = RuntimeUtil.execForStr(getVersionCmd);
        System.out.println(version);
    }
}