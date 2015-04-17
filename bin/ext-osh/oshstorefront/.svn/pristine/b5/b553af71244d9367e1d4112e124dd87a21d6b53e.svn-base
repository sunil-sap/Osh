ACCMOB.facets = {
		params:					{},
		cachedJSONResponse:		{},
		currentSelection:		{},
		currentSearchQuery:		"",

		// begin DATA -------------------------------------------------------------------
		setCurrentSearchQuery: function(untouched) {
			var pageQuery = $("#facetRefinements-page").data("searchquery");
			if (untouched == null) {  // set default value
				var untouched = false;
			}

			if (typeof pageQuery !== 'undefined') {
				if (untouched) {
					ACCMOB.facets.currentSearchQuery = pageQuery
				} else {
					ACCMOB.facets.currentSearchQuery = pageQuery.split(":").slice(0, 2).join(":"); // take only searchterm and sorting
				}
			}
		},

		createQueryParams: function(initialCreation) {
			if (initialCreation == null) {  // set default value
				var initialCreation = false;
			}

			ACCMOB.facets.params = {};
			var selectedOptions = $("fieldset.facetValueList input:checked");
			var facets = selectedOptions.map(function() { return $(this).data("query"); }).get().join(":");

			if (facets !== null && facets !== [] && facets !== "") {
				ACCMOB.facets.params = {q: [ACCMOB.facets.currentSearchQuery, facets].join(":")};
			} else {
				if (initialCreation) {
					ACCMOB.facets.setCurrentSearchQuery(true);
				}
				ACCMOB.facets.params = {q: ACCMOB.facets.currentSearchQuery};
				if (initialCreation) {
					ACCMOB.facets.setCurrentSearchQuery();
				}
			}
			;
		},

		getFacetData:    function() {
			$.mobile.showPageLoadingMsg();
			$.ajax({
				url:      ACCMOB.common.currentPath + "/facets",
				dataType: "json",
				async:    false,
				data:     ACCMOB.facets.params,
				success:  function(data) { ACCMOB.facets.cachedJSONResponse = data; } // cache data
			});
			$.mobile.hidePageLoadingMsg();

		},
		// end DATA ---------------------------------------------------------------------

		// begin BINDINGS ---------------------------------------------------------------
		bindUpdateFacet: function(trigger, eventName) {
			trigger.live(eventName, function(event) {
				ACCMOB.facets.updateRefinementsList();
				$("#facetRefinements-page ul").listview();
				ACCMOB.facets.renderResultsCount(ACCMOB.facets.cachedJSONResponse.count);
				ACCMOB.facets.updateFacetContents();
				$("div[data-role='content'] input").checkboxradio();
				$("div[data-role='content'] fieldset").controlgroup();
				$("div[data-role='content']").addClass("ui-content ui-body-d");
			});
		},

		bindClearFacetSelections: function(clearFacetSelectionsButton) {
			ACCMOB.facets.setCurrentSearchQuery();

			clearFacetSelectionsButton.live("click", function() {
				$(this).attr("href", "?" + $.param({q: ACCMOB.facets.currentSearchQuery}));
			});

		},

		bindApplyFilter: function(applyFilterButton) {
			applyFilterButton.live("click", function() {
				ACCMOB.facets.createQueryParams();
				$(this).attr("href", "?" + $.param({q: ACCMOB.facets.params.q}));
			});
		},

		bindAddFilterButton: function(addFilterButton) {
			addFilterButton.live("click", function() {
				with(ACCMOB.facets) {
					updateRefinementsList(true);
					renderResultsCount(ACCMOB.facets.cachedJSONResponse.count);
					renderFacetPages();
				}
			});
			// set status icon
			var pageQuery = $("#facetRefinements-page").data("searchquery");
			if (pageQuery.split(":")[2] !== undefined) {
				addFilterButton.find(".ui-btn-inner span:last").removeClass("ui-icon-checkbox-off").addClass("ui-icon-checkbox-on");
			}
		},

		setTemplates:       function() {
			this.refinementsListTemplate = $("#refinementsListTemplate");
			this.refinementFacetPageTemplate = $("#refinementFacetPageTemplate");
			this.refinementFacetContentTemplate = $("#refinementFacetContentTemplate");
		},
		// end BINDINGS ---------------------------------------------------------------

		// begin RENDER ---------------------------------------------------------------
		renderResultsCount: function(resultCount) { $(".resultsCount").html(resultCount); },

		renderFacetPages: function() {
			$.each(ACCMOB.facets.cachedJSONResponse.facets, function(index, facet) {
				facet.count = ACCMOB.facets.cachedJSONResponse.count;  // add full count
				ACCMOB.facets.renderFacetPage(facet);
				ACCMOB.facets.renderFacetLink(facet);
			});
		},

		renderFacetLink: function(facet) {
			// collect all selected values of facet
			var facetNames = [];
			$.each(facet.values, function(i, facetValue){
				if (facetValue.selected) { facetNames.push(facetValue.name); }
			});
			// merge names of selected facetvalues and append to button
			$("#"+facet.name+"-button").append("<span class=ui-li-aside>" + facetNames.join(", ") + "</span>");
		},

		renderFacetPage: function(facet) {
			$.tmpl(ACCMOB.facets.refinementFacetPageTemplate, facet).appendTo("body");
		},

		renderRefinementsList: function() {
			$("div#facetRefinements-page div[data-role='content'] ul").replaceWith($.tmpl(ACCMOB.facets.refinementsListTemplate, ACCMOB.facets.cachedJSONResponse));
		},
		// end RENDER -----------------------------------------------------------------

		// begin UPDATE ---------------------------------------------------------------
		updateFacetContents:   function() {
			$.each(ACCMOB.facets.cachedJSONResponse.facets, function(index, facet) {
				facet.count = ACCMOB.facets.cachedJSONResponse.count;  // add full count
				ACCMOB.facets.updateFacetContent(facet);
				ACCMOB.facets.renderFacetLink(facet);
			});
		},

		updateFacetContent: function(facet) {
			var facetPageContent = $("#" + facet.name + "-page > div[data-role='content']");

			facetPageContent.replaceWith($.tmpl(ACCMOB.facets.refinementFacetContentTemplate, facet));
			facetPageContent.find("input").checkboxradio();
		},

		updateRefinementsList: function(initialCreation) {
			if (initialCreation == null) {
				var initialCreation = false; // set default value
			}

			ACCMOB.facets.createQueryParams(initialCreation);
			ACCMOB.facets.getFacetData();
			ACCMOB.facets.renderRefinementsList();
		},
		// end UPDATE ---------------------------------------------------------------

		redirectIfPageWasReloaded: function() {
			var currentUrl = $.mobile.path.parseUrl(window.location.href);

			if (currentUrl.hash !== "" && currentUrl.hash.match(/-page$/)) {

				if (currentUrl.hash === "#facetRefinements-page") {
					history.go(-1);
				} else {
					history.go(-2);
				}
				return false;
			}
			return true;
		}
};

$(document).ready(function() {

	ACCMOB.facets.redirectIfPageWasReloaded();

	with(ACCMOB.facets) {
		bindAddFilterButton($("#addFilters"));
		setTemplates();
		setCurrentSearchQuery();

		bindUpdateFacet($(".facetPage input"), "change");
		bindUpdateFacet($(".multiSelectFacetPage a.backToFacets"), "click");

		bindApplyFilter($("#applyFilter"));
		bindClearFacetSelections($("#clearFacetSelections"));
	}

});