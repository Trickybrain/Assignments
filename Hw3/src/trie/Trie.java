package trie;
import java.util.*;

public class Trie {
    private static final int CHARACTER_SET_SIZE = 128;
    TrieNode root; // Acts as a sentinel node

    public static class TrieNode{
        char letter;
        boolean isKey;
        TrieNode[] children;

        public TrieNode(char k, boolean keyFlag){
            this.letter = k;
            this.isKey = keyFlag;
            children = new TrieNode[CHARACTER_SET_SIZE];
        }

        public boolean isLeaf(){
            for(int i = 0; i < CHARACTER_SET_SIZE; i++){
                if(children[i] != null){
                    return false;
                }
            }
            return true;
        }
    }

    public Trie(){
        //Initialize root to take on any value.
        root = new TrieNode('a', false);
    }

    //Iteratively add String s to the trie
    public void insert(String s){
        if(s == null){
            return;
        }
        int stringLength = s.length();
        TrieNode traverser = root;

        for(int idx = 0; idx < stringLength; idx ++){
            char currentLetter = s.charAt(idx);
            if(traverser.children[currentLetter] == null){
                TrieNode childNode = new TrieNode(currentLetter, false);
                traverser.children[currentLetter] = childNode;
            }
            traverser = traverser.children[currentLetter];
        }
        traverser.isKey = true;
    }

    //Iterative implementation to check if trie contains s
    public boolean contains(String s){
        if(s == null){
            return false;
        }
        int stringLength = s.length();
        TrieNode parent = root;

        for(int idx = 0; idx < stringLength; idx ++){
            char currentLetter = s.charAt(idx);
            if(parent.children[currentLetter] == null){
                return false;
            }
            parent = parent.children[currentLetter];
        }
        return parent.isKey;
    }

    public void delete(String s){
        if(contains(s)){
            if(s.length() == 0){
                root.isKey = false;
            } else {
                deleteHelper(s, s.length(), 0, null, root);
            }
        }
    }

    public void deleteHelper(String s, int sLen, int curIdx, TrieNode parent, TrieNode curNode){
        if(curIdx == sLen){
            char lastLetter = s.charAt(sLen - 1);
            if(curNode.isLeaf() && parent != null){
                parent.children[lastLetter] = null;
            } else {
                curNode.isKey = false;
            }
        } else {
            deleteHelper(s, sLen, curIdx + 1, curNode, curNode.children[s.charAt(curIdx)]);
            if(curNode.isLeaf()  && !curNode.isKey) {
                char letterOfInterest = s.charAt(curIdx - 1);
                parent.children[letterOfInterest] = null;
            }
        }
    }

    // Return a list of all the keys that are in the trie
    public List<String> collectKeys() {
        List<String> allKeys = new ArrayList<>();
        collectKeysHelper(root, "", allKeys);
        return allKeys;
    }

    private void collectKeysHelper(TrieNode node, String prefix, List<String> keys) {
        if (node == null) {
            return;
        }

        if (node.isKey) {
            keys.add(prefix);
        }

        for (char c = 0; c < CHARACTER_SET_SIZE; c++) {
            TrieNode child = node.children[c];
            if (child != null) {
                collectKeysHelper(child, prefix + c, keys);
            }
        }
    }

    // Return a list of all the keys that are in the trie
    public List<String> keysWithPrefix(String prefix) {
        List<String> prefixKeys = new ArrayList<>();
        TrieNode prefixNode = findPrefixNode(prefix);
        collectKeysHelper(prefixNode, prefix, prefixKeys);
        return prefixKeys;
    }

    private TrieNode findPrefixNode(String prefix) {
        int length = prefix.length();
        TrieNode node = root;

        for (int i = 0; i < length; i++) {
            char currentLetter = prefix.charAt(i);
            if (node.children[currentLetter] == null) {
                return null;
            }
            node = node.children[currentLetter];
        }

        return node;
    }
}
