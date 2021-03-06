package ro.fortsoft.genericdata.utils.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Transformer;

public class QueryParameter implements Serializable {

	private static final long serialVersionUID = -8611407316504601939L;

	public static final String EQ = "eq";
	
	public static final String IEQ = "ieq";

	public static final String NE = "ne";

	public static final String GT = "gt";

	public static final String LT = "lt";

	public static final String GE = "ge";

	public static final String LE = "le";

	public static final String IN = "in";

	public static final String NOT_IN = "notIn";

	public static final String BETWEEN = "between";

	public static final String IS_NULL = "isNull";

	public static final String IS_NOT_NULL = "isNotNull";

	public static final String LIKE = "like";

	public static final String LIKE_EXACT = "likeExact";

	public static final String LIKE_START = "likeStart";

	public static final String LIKE_END = "likeEnd";

	public static final String ILIKE = "ilike";

	public static final String LIKE_ANYWHERE = "likeAnywhere";

	public static final String ILIKE_EXACT = "ilikeExact";

	public static final String ILIKE_START = "ilikeStart";

	public static final String ILIKE_END = "ilikeEnd";

	public static final String ILIKE_ANYWHERE = "ilikeAnywhere";

	public static final String SQL = "sql";

	public static final String OR_RESTRICTIONS = "orRestrictions";

	public static final int FILTER_STATE_IGNORECASE_ENABLELIKE = 1;

	public static final int FILTER_STATE_IGNORECASE_NOENABLELIKE = 2;

	public static final int FILTER_STATE_NOIGNORECASE_ENABLELIKE = 3;

	public static final int FILTER_STATE_NOIGNORECASE_NOENABLELIKE = 4;

	public static final int QUERY_TYPE_NONE = 0;

	public static final int QUERY_TYPE_EXACT = 1;

	public static final int QUERY_TYPE_WITHESCAPECHARACTER = 2;

	public static final char ESCAPE_CHARACTER = '\\';

	private int filterState = FILTER_STATE_IGNORECASE_ENABLELIKE;

	private int queryType = QUERY_TYPE_NONE;

	private final List<String> defaultExcludedProperties = Arrays.asList("createdBy", "creationDate", "lastUpdatedBy",
			"lastUpdateDate", "deleted");

	private final List<String> excludedProperties;

	private final List<String> projectionProperties;

	private String propertyCountBy;

	private Long first;

	private Long count;

	private final List<Sorter> sorters;

	private final List<ValueRestriction> restrictions;

	private boolean withPersistentRestrictions = true;

	private boolean withDistinctProjection = true;

	private Transformer resultTransformer;

	public static final int MATCH_MODE_EXACT = 1;

	public static final int MATCH_MODE_START = 2;

	public static final int MATCH_MODE_END = 3;

	public static final int MATCH_MODE_ANYWHERE = 4;

	private int matchMode = MATCH_MODE_EXACT;

	private boolean distinctRootEntity;

	public static final String INITIALIZE_ALL = "INITIALIZE_ALL";
	private final List<String> lazyFieldsToInitialize;

	public QueryParameter() {
		super();
		sorters = new ArrayList<Sorter>();
		restrictions = new ArrayList<ValueRestriction>();
		excludedProperties = new ArrayList<String>();
		projectionProperties = new ArrayList<String>();
		lazyFieldsToInitialize = new ArrayList<String>();
	}

	public static QueryParameter build() {
		return new QueryParameter();
	}


    /**
     * Constructor.
     *
     * @param field the sorting field
     */
	public QueryParameter(final String field) {
		this(field, true);
	}

	public static QueryParameter build(final String field) {
		return new QueryParameter(field);
	}

	public QueryParameter(final String field, final boolean ascending) {
		this();
		final Sorter sorter = new Sorter(field, ascending);
		sorters.add(sorter);
	}

	public static QueryParameter build(final String field, final boolean ascending) {
		return new QueryParameter(field, ascending);
	}

	public Long getFirst() {
		return first;
	}

	public QueryParameter setFirst(final Long first) {
		this.first = first;
		return this;
	}

	public Long getCount() {
		return count;
	}

	public QueryParameter setCount(final Long count) {
		this.count = count;
		return this;
	}

	public String getSort() {
		if (sorters.isEmpty()) {
			return null;
		}
		return sorters.get(0).getField();
	}

	public QueryParameter setSort(final String sort) {
		if (sorters.isEmpty()) {
			final Sorter sorter = new Sorter(sort, true);
			sorters.add(sorter);
		} else {
			final Sorter sorter = sorters.get(0);
			sorter.setField(sort);
		}
		return this;
	}

	public boolean isSortAsc() {
		if (sorters.isEmpty()) {
			return true;
		}
		return sorters.get(0).isAscending();
	}

	public QueryParameter setSortAsc(final boolean sortAsc) {
		if (!sorters.isEmpty()) {
			final Sorter sorter = sorters.get(0);
			sorter.setAscending(sortAsc);
		}
		return this;
	}

	public QueryParameter setFilterState(final int filterState) {
		this.filterState = filterState;
		return this;
	}

	public int getFilterState() {
		return filterState;
	}

	public int getQueryType() {
		return queryType;
	}

	public QueryParameter setQueryType(final int queryType) {
		this.queryType = this.queryType | queryType;
		return this;
	}

	public boolean hasSort() {
		return !sorters.isEmpty();
	}

	public List<Sorter> getSorters() {
		return sorters;
	}

	public List<ValueRestriction> getRestrictions() {
		return restrictions;
	}

	@Deprecated
	public QueryParameter addSorter(final String sort) {
		sorters.add(new Sorter(sort, true));
		return this;
	}

	@Deprecated
	public QueryParameter addSorter(final Sorter sorter) {
		sorters.add(sorter);
		return this;
	}

	@Deprecated
	public QueryParameter addSorter(final String field, final boolean ascending) {
		sorters.add(new Sorter(field, ascending));
		return this;
	}
	
	public QueryParameter orderBy(final String sort) {
		sorters.add(new Sorter(sort, true));
		return this;
	}

	public QueryParameter orderBy(final Sorter sorter) {
		sorters.add(sorter);
		return this;
	}

	public QueryParameter orderBy(final String field, final boolean ascending) {
		sorters.add(new Sorter(field, ascending));
		return this;
	}
	
	@Deprecated
	public QueryParameter addRestriction(final ValueRestriction restriction) {
		return and(restriction);
	}

	@Deprecated
	public QueryParameter addRestriction(final String field, final String operator, final Object value) {
		return and(field, operator, value);
	}

	@Deprecated
	public QueryParameter addRestriction(final String field, final String operator) {
		return and(field, operator);
	}

	@Deprecated
	public QueryParameter addRestriction(final String sql) {
		return and(sql);
	}

	public QueryParameter where(final ValueRestriction restriction) {
		return and(restriction);
	}

	public QueryParameter where(final String field, final String operator, final Object value) {
		return and(field, operator, value);
	}

	public QueryParameter where(final String field, final String operator) {
		return and(field, operator);
	}

	public QueryParameter where(final String sql) {
		return and(sql);
	}

	public QueryParameter and(final ValueRestriction restriction) {
		restrictions.add(restriction);
		return this;
	}

	public QueryParameter and(final String field, final String operator, final Object value) {
		restrictions.add(new ValueRestriction(field, operator, value));
		return this;
	}

	public QueryParameter and(final String field, final String operator) {
		restrictions.add(new ValueRestriction(field, operator));
		return this;
	}

	public QueryParameter and(final String sql) {
		restrictions.add(new ValueRestriction(sql));
		return this;
	}

	public QueryParameter resetSorters() {
		sorters.clear();
		return this;
	}

	public QueryParameter resetRestrictions() {
		restrictions.clear();
		return this;
	}

	public QueryParameter resetExcludedProperties() {
		excludedProperties.clear();
		return this;
	}

	public List<String> getExcludedProperties() {
		return excludedProperties;
	}

	public QueryParameter addProjectionProperty(final String property) {
		projectionProperties.add(property);
		return this;
	}

	public List<String> getProjectionProperties() {
		return projectionProperties;
	}

	public QueryParameter resetDistinctProperties() {
		projectionProperties.clear();
		return this;
	}

	public QueryParameter addExtraExcludedProperties(final List<String> excludeProperties, final boolean withDefault) {
		queryType = queryType | QUERY_TYPE_EXACT;
		if (withDefault) {
			excludedProperties.addAll(defaultExcludedProperties);
		}
		excludedProperties.addAll(excludeProperties);
		return this;
	}

	public boolean isWithPersistentRestrictions() {
		return withPersistentRestrictions;
	}

	public QueryParameter withPersistentRestrictions(final boolean withPersistentRestrictions) {
		this.withPersistentRestrictions = withPersistentRestrictions;
		return this;
	}

	public boolean isWithDistinctProjection() {
		return withDistinctProjection;
	}

	public QueryParameter setWithDistinctProjection(final boolean withDistinctProjection) {
		this.withDistinctProjection = withDistinctProjection;
		return this;
	}

	public String getPropertyCountBy() {
		return propertyCountBy;
	}

	public QueryParameter setPropertyCountBy(final String propertyCountBy) {
		this.propertyCountBy = propertyCountBy;
		return this;
	}

	public Transformer getResultTransformer() {
		return resultTransformer;
	}

	public int getMatchMode() {
		return matchMode;
	}

	public void setMatchMode(final int matchMode) {
		this.matchMode = matchMode;
	}

	public QueryParameter setResultTransformer(final Transformer resultTransformer) {
		this.resultTransformer = resultTransformer;
		return this;
	}

	public QueryParameter addLazyFieldToInitialize(final String property) {
		lazyFieldsToInitialize.add(property);
		return this;
	}

	public List<String> getLazyFieldsToInitialize() {
		return lazyFieldsToInitialize;
	}

	public QueryParameter resetLazyFieldsToInitialize() {
		lazyFieldsToInitialize.clear();
		return this;
	}

	public boolean isDistinctRootEntity() {
		return distinctRootEntity;
	}

	public QueryParameter setDistinctRootEntity(final boolean distinctRootEntity) {
		this.distinctRootEntity = distinctRootEntity;
		return this;
	}

}
