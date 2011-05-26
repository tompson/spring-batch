/*
 * Copyright 2006-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.item.database.support;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Thomas Risberg
 */
public class SqlWindowingPagingQueryProviderTests extends AbstractSqlPagingQueryProviderTests {

	public SqlWindowingPagingQueryProviderTests() {
		pagingQueryProvider = new SqlWindowingPagingQueryProvider();
	}

	@Test @Override
	public void testGenerateFirstPageQuery() {
		String sql = "SELECT * FROM ( SELECT id, name, age, ROW_NUMBER() OVER (ORDER BY id ASC) AS ROW_NUMBER FROM foo WHERE bar = 1) WHERE ROW_NUMBER <= 100";
		String s = pagingQueryProvider.generateFirstPageQuery(pageSize);
		assertEquals("", sql, s);
	}

	@Test @Override
	public void testGenerateRemainingPagesQuery() {
		String sql = "SELECT * FROM ( SELECT id, name, age, ROW_NUMBER() OVER (ORDER BY id ASC) AS ROW_NUMBER FROM foo WHERE bar = 1 AND id > ?) WHERE ROW_NUMBER <= 100";
		String s = pagingQueryProvider.generateRemainingPagesQuery(pageSize);
		assertEquals("", sql, s);
	}

	@Test @Override
	public void testGenerateJumpToItemQuery() {
		String sql = "SELECT SORT_KEY FROM ( SELECT id AS SORT_KEY, ROW_NUMBER() OVER (ORDER BY id ASC) AS ROW_NUMBER FROM foo WHERE bar = 1) WHERE ROW_NUMBER = 100";
		String s = pagingQueryProvider.generateJumpToItemQuery(145, pageSize);
		assertEquals("", sql, s);
	}

	@Test @Override
	public void testGenerateJumpToItemQueryForFirstPage() {
		String sql = "SELECT SORT_KEY FROM ( SELECT id AS SORT_KEY, ROW_NUMBER() OVER (ORDER BY id ASC) AS ROW_NUMBER FROM foo WHERE bar = 1) WHERE ROW_NUMBER = 1";
		String s = pagingQueryProvider.generateJumpToItemQuery(45, pageSize);
		Assert.assertEquals("", sql, s);
	}
	
	@Test
	@Override
	public void testGenerateFirstPageQueryWithGroupBy() {
		pagingQueryProvider.setGroupClause("dep");
		String sql = "SELECT * FROM ( SELECT id, name, age, ROW_NUMBER() OVER (ORDER BY id ASC) AS ROW_NUMBER FROM foo WHERE bar = 1 GROUP BY dep) WHERE ROW_NUMBER <= 100";
		String s = pagingQueryProvider.generateFirstPageQuery(pageSize);
		assertEquals("", sql, s);
	}

	@Test @Override
	public void testGenerateRemainingPagesQueryWithGroupBy() {
		pagingQueryProvider.setGroupClause("dep");
		String sql = "SELECT * FROM ( SELECT id, name, age, ROW_NUMBER() OVER (ORDER BY id ASC) AS ROW_NUMBER FROM foo WHERE bar = 1 AND id > ? GROUP BY dep) WHERE ROW_NUMBER <= 100";
		String s = pagingQueryProvider.generateRemainingPagesQuery(pageSize);
		assertEquals("", sql, s);
	}

	@Test @Override
	public void testGenerateJumpToItemQueryWithGroupBy() {
		pagingQueryProvider.setGroupClause("dep");
		String sql = "SELECT SORT_KEY FROM ( SELECT id AS SORT_KEY, ROW_NUMBER() OVER (ORDER BY id ASC) AS ROW_NUMBER FROM foo WHERE bar = 1 GROUP BY dep) WHERE ROW_NUMBER = 100";
		String s = pagingQueryProvider.generateJumpToItemQuery(145, pageSize);
		assertEquals("", sql, s);
	}

	@Test @Override
	public void testGenerateJumpToItemQueryForFirstPageWithGroupBy() {
		pagingQueryProvider.setGroupClause("dep");
		String sql = "SELECT SORT_KEY FROM ( SELECT id AS SORT_KEY, ROW_NUMBER() OVER (ORDER BY id ASC) AS ROW_NUMBER FROM foo WHERE bar = 1 GROUP BY dep) WHERE ROW_NUMBER = 1";
		String s = pagingQueryProvider.generateJumpToItemQuery(45, pageSize);
		Assert.assertEquals("", sql, s);
	}

}
