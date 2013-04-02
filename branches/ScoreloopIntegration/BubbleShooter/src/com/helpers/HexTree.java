package com.helpers;

public abstract class HexTree<E> {
	private Position<E>[] roots = (Position<E>[]) new Object[2];
	
	public HexTree() {
		
	}
	
	/**
     * Replaces the element at the position with the specified element,
     * and returns the replaced element.
     * Requires: The position is valid.
     */
    public abstract E replace(Position<E> p, E e);
    
    /**
     * Returns the left root of the binary tree.
     * Requires: The binary tree is not empty.
     */
    public abstract Position<E> getLeftRoot();
    
    /**
     * Returns the right root of the binary tree.
     * Requires: The binary tree is not empty.
     */
    public abstract Position<E> getRightRoot();
    
    /**
     * Returns the left parent of the position.
     * Requires: The position is valid and not a root.
     */
    public abstract Position<E> getLeftParent(Position<E> p);
    
    /**
     * Returns the right parent of the position.
     * Requires: The position is valid and not a root.
     */
    public abstract Position<E> getRightParent(Position<E> p);
    
    /**
     * Returns whether the position is an internal node.
     * Requires: The position is valid.
     */
    public abstract boolean isInternal(Position<E> p);
    
    /**
     * Returns whether the position is an external node.
     * Requires: The position is valid.
     */
    public abstract boolean isExternal(Position<E> p);
    
    /**
     * Returns whether the position is a root of the binary tree.
     * Requires: The position is valid.
     */
    public abstract boolean isRoot(Position<E> p);
    
    /**
     * Adds the element as root and returns the position of this root.
     * Requires: The binary tree is empty.
     */
    public abstract Position<E> addRoot(E e);
    
    /**
     * Returns the left child of the position.
     * Requires: The position is valid and has a left child.
     */
    public abstract Position<E> getLeftChild(Position<E> p);
    
    /**
     * Returns the right child of the position.
     * Requires: The position is valid and has a right child.
     */
    public abstract Position<E> getRightChild(Position<E> p);
    
    /**
     * Returns whether the position has a left child.
     * Requires: The position is valid.
     */
    public abstract boolean hasLeftChild(Position<E> p);
    
    /**
     * Returns whether the position has a right child.
     * Requires: The position is valid.
     */
    public abstract boolean hasRightChild(Position<E> p);
    
    /**
     * Inserts the element as left child of the position,
     * and returns the position of the new left child.
     * Requires: The position is valid and does not have a left child.
     */
    public abstract Position<E> insertLeftChild(Position<E> p, E e);
    
    /**
     * Inserts the element as right child of the position,
     * and returns the position of the new right child.
     * Requires: The position is valid and does not have a right child.
     */
    public abstract Position<E> insertRightChild(Position<E> p, E e);
    
    /**
     * Removes and returns the element at the position.
     * Requires: The position is valid and has at most one child.
     */
    public abstract E remove(Position<E> p);
    
    /**
     * Returns the left sibling of the position.
     * Requires: The position is valid and has a left sibling.
     */
    public abstract Position<E> getLeftSibling(Position<E> p);
    
    /**
     * Returns the right sibling of the position.
     * Requires: The position is valid and has a right sibling.
     */
    public abstract Position<E> getRightSibling(Position<E> p);
    
    /**
     * Inserts the element as left sibling of the position,
     * and returns the position of the new left sibling.
     * Requires: The position is valid and does not have a left sibling.
     */
    public abstract Position<E> insertLeftSibling(Position<E> p, E e);
    
    /**
     * Inserts the element as right sibling of the position,
     * and returns the position of the new right sibling.
     * Requires: The position is valid and does not have a right sibling.
     */
    public abstract Position<E> insertRightSibling(Position<E> p, E e);
    
//    /**
//     * Adds the trees t1 and t2 as left and right subtree of the position. 
//     * Requires: The position is valid and is an external node.
//     */
//    public abstract void attach(Position<E> p, HexTree<E> t1, HexTree<E> t2);
	
	
	public interface Position<E> {
		public E getElement();
	}
	
	protected class Node implements Position<E> {
		private static final int LEFT = 0;
		private static final int RIGHT = 1;
		private Node[] parents = (Node[]) new Object[2];
		private Node[] siblings = (Node[]) new Object[2];
		private Node[] children = (Node[]) new Object[2];
		private E element;
		
		public Node() {	}
		
		public Node(E e) {
			element = e;
		}
		
		public Node getLeftParent() {
			return parents[LEFT];
		}
		
		public void setLeftParent(Node node) {
			parents[LEFT] = node;
		}
		
		public Node getRightParent() {
			return parents[RIGHT];
		}
		
		public void setRightParent(Node node) {
			parents[RIGHT] = node;
		}
		
		public Node getLeftSibling() {
			return siblings[LEFT];
		}
		
		public void setLeftSibling(Node node) {
			parents[LEFT] = node;
		}
		
		public Node getRightSibling() {
			return siblings[RIGHT];
		}
		
		public void setRightSibling(Node node) {
			parents[RIGHT] = node;
		}
		
		public Node getLeftChild() {
			return children[LEFT];
		}
		
		public void setLeftChild(Node node) {
			parents[LEFT] = node;
		}
		
		public Node getRightChild() {
			return children[RIGHT];
		}		
		
		public void setRightChild(Node node) {
			parents[RIGHT] = node;
		}
		
		public void setElement(E e) {
			element = e;
		}
		
		@Override
		public E getElement() {
			return element;
		}
	}
}
