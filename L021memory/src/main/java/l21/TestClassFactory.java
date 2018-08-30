package l21;

public class TestClassFactory implements GeneralObjectFactory {
    public Object get(int... args) {
        return new MyTestClass();
    }
}
