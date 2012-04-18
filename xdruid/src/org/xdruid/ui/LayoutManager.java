package org.xdruid.ui;

public interface LayoutManager {
	public int getLayoutId(String name);
	public int getLayoutId(String name, int width, int height);
	public void register(String name, int layoutId, int width, int height);
	public void registerDefaultLayout(String name, int layoutId);
}
