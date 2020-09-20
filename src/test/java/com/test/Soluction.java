package com.test;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class Solution {
    public int lengthOfLongestSubstring(String s) {
        int cursor1 = 0;
        int cursor2 = 0;
        int length = 0;
        Set<Character> set = new HashSet<Character>();
        int n = s.length();
        char[] str = s.toCharArray();
        while (cursor2 <= n - 1) {
            if (set.contains(str[cursor2])) {
                set.remove(str[cursor1]);
                ++cursor1;
            } else {
                set.add(str[cursor2]);
                ++cursor2;
                int thislength = set.size();
                if (thislength >= length)
                    length = thislength;
            }
        }
        return length;
    }

    @Test
    public void test() {
        int length = lengthOfLongestSubstring("");
        System.out.println(length);
    }
}
