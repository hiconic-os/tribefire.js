// ============================================================================
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.gwt.geom.client;

/**
 * 2D Point class that combines x and y coordinates and has
 * some convenience methods.
 * @author Dirk
 *
 */
public class Point {
	private double x;
	private double y;
	
	public Point() {
		this(0, 0);
	}
	
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p) {
		this(p.getX(), p.getY());
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Point getScaledInstance(Point origin, double factor) {
		return new Point(
				origin.getX() + (x - origin.getX()) * factor,
				origin.getY() + (y - origin.getY()) * factor);
	}
	
	public double distance(Point p) {
		double dx = p.getX() - getX();
		double dy = p.getY() - getY();
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public Point subtract(Point p) {
		return new Point(getX() - p.getX(), getY() - p.getY());
	}
	
	@Override
	public String toString() {
		return "[x="+x + ", y="+y+"]";
	}

	public void setLocation(double x, double y) {
		setX(x);
		setY(y);
	}
}
