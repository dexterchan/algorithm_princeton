package SubString;

/*
   Prefix free codes. In data compression,
    a set of binary strings is  if no string is a prefix of another.
    For example, {01,10,0010,1111} is prefix free, but {01,10,0010,10100}
    is not because 10 is a prefix of 10100. Design an efficient algorithm to determine
    if a set of binary strings is prefix-free.
    The running time of your algorithm should be proportional the number of bits
    in all of the binary stings.
    */
/*
1) Construct Tries from binary string pool
2) if we find thge nth digit of a substring already has a Tries Node, we stop

 */

class TriesException extends IllegalArgumentException {
    String message;

    public TriesException(String message) {
        super();
        this.message = message;
    }
}

class BinaryTries {

    private Node node;

    public BinaryTries() {
        this.node = new Node();
    }

    private final static class Node {
        private Object value;
        private Node[] nodes = new Node[2];

        Node() {
            this(null);
        }

        Node(Object value) {
            this.value = value;
        }

        private boolean hasNode(int value) {
            BinaryTries.isDigitValid(value);
            if (nodes[value] == null) return false;
            return true;
        }

        private Node getNode(int value) {
            BinaryTries.isDigitValid(value);
            return nodes[value];
        }

        private String replaceValue(String value) {
            this.value = value;
            return value;
        }

        private boolean hasValue() {
            return this.value != null;
        }

        private Node insertNode(int digit, String finalString) {
            Node nextNode;
            if (this.hasNode(digit)) {
                nextNode = this.nodes[digit];
                if (nextNode.hasValue()) nextNode.replaceValue(finalString);
                return nextNode;
            }
            nextNode = new Node(finalString);
            this.nodes[digit] = nextNode;
            return nextNode;
        }

    }

    private static boolean isDigitValid(int value) {
        if (value < 0 || value > 1) {
            throw new TriesException("digit value of our bound" + value);
        }
        ;
        return true;
    }


    public boolean insertString(String binaryDigits) {
        if (binaryDigits == null) throw new IllegalArgumentException("Null binary digits");
        boolean duplicated = false;
        Node currentNode = this.node;
        int totalLength = binaryDigits.length();
        for (int i = 0; i < totalLength; i++) {
            int digit = deriveDigit(binaryDigits, i);

            if (isTriesPathLastDigitAndIncludingThisDigit(currentNode, digit)) duplicated = true;
            if (isLastDigit(binaryDigits, i) && isDigitIncludedInTries(currentNode, digit)) duplicated = true;
            currentNode = currentNode.insertNode(digit, i == totalLength - 1 ? binaryDigits : null);
        }
        return duplicated;
    }

    private int deriveDigit(String binaryDigit, int i){
        return binaryDigit.charAt(i) - '0';
    }

    private boolean isTriesPathLastDigitAndIncludingThisDigit(Node node, int digit){
        return node.hasNode(digit) && node.getNode(digit).hasValue();
    }

    private boolean isDigitIncludedInTries(Node node, int digit){
        return node.hasNode(digit);
    }

    private boolean isLastDigit(String binaryDigits, int pos) {
        return binaryDigits.length() - 1 == pos;
    }
}

public class Prefix_Binary {
    public static void main(String[] args) {
        testPatternWithoutPrefix();
        testPatternWithPrefix();
    }

    public static boolean detectPrefixInDigitStrings(String[] digitStrings) {
        BinaryTries binaryTries = new BinaryTries();
        for (String digitString : digitStrings) {
            boolean foundPrefixDuplicated = binaryTries.insertString(digitString);
            if (foundPrefixDuplicated) return true;
        }
        return false;
    }

    private static void testPatternWithoutPrefix() {
        String[] prefixFree = {"01", "10", "0010", "1111"};
        boolean foundPrefix = Prefix_Binary.detectPrefixInDigitStrings(prefixFree);
        if (foundPrefix) throw new IllegalStateException();

        String[] prefixFree2 = {"0010", "1111", "01", "10"};
        foundPrefix = Prefix_Binary.detectPrefixInDigitStrings(prefixFree2);
        if (foundPrefix) throw new IllegalStateException();

        String[] prefixFree3 = {"0010", "1111", "01", "10","00111"};
        foundPrefix = Prefix_Binary.detectPrefixInDigitStrings(prefixFree3);
        if (foundPrefix) throw new IllegalStateException();
    }

    private static void testPatternWithPrefix() {
        String[] prefixFree = {"01", "10", "0010", "10100"};
        boolean foundPrefix = Prefix_Binary.detectPrefixInDigitStrings(prefixFree);
        assert foundPrefix;

        String[] prefixFree2 = {"0010", "10100", "01", "10"};
        foundPrefix = Prefix_Binary.detectPrefixInDigitStrings(prefixFree2);
        assert foundPrefix;

        String[] prefixFree3 = {"0010", "1111", "01", "10","00111", "0011"};
        foundPrefix = Prefix_Binary.detectPrefixInDigitStrings(prefixFree3);
        assert foundPrefix;
    }
}
