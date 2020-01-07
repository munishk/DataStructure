package stack;

public class MyStack<T> {
	
	int top;
	Object[] values;
	
	public MyStack(int size) {
		values = new Object[size];
		top = -1;
	}

	
	public void push(T value) {
		if(top == values.length -1) {
			throw new RuntimeException("Stack is full");
		}
		values[++top] = value;
	}
	
	@SuppressWarnings("unchecked")
	public T pop() {
		if(top == -1) {
			throw new RuntimeException("Stack is empty");
		}
		return (T) values[top--];
	}
	
	@SuppressWarnings("unchecked")
	public T peek() {
		if(top == -1) {
			throw new RuntimeException("Stack is empty");
		}
		return (T) values[top];
	}
	
	public int size() {
		return top+1;
	}
	
	public boolean isEmpty() {
		return top ==-1;
	}
	
	public boolean isFull() {
		return top == values.length -1;
	}
	
	public void print() {
		
		for(int i=0; i <=top; i++) {
			System.out.println(values[i]);
		}
	}
	
	public static void main(String[] args) {
		MyStack<String> myStack = new MyStack<>(10);
		myStack.push("manish");
		myStack.push("Suranjan");
		myStack.push("ravi");
		
		myStack.print();
		
		while(!myStack.isEmpty()) {
			System.out.println("Popped item:" + myStack.pop());
		}
		
	}
}
