ACCMOB.productlisting = {
		infiniteScrollingConfig: {offset: '100%'},
		currentPage:			0,
		numberOfPages:			Number.MAX_VALUE,
		baseQuery:				$("#sort_form1 input[type='hidden']").val() || "",

		triggerLoadMoreResults: function() {
			if (ACCMOB.productlisting.currentPage < ACCMOB.productlisting.numberOfPages) {
				// show the page loader
				$.mobile.showPageLoadingMsg();
				if (ACCMOB.productlisting.numberOfPages !== null) {
					ACCMOB.productlisting.loadMoreResults(ACCMOB.productlisting.currentPage + 1);
				}
				else {
					ACCMOB.productlisting.loadMoreResults();
				}
			}
		},

		scrollingHandler: function(event, direction) {
			if (direction === "down") {
				ACCMOB.productlisting.triggerLoadMoreResults();
			}
		},

		loadMoreResults: function(page) {
			if (page == null) {
				page = 1;  // set default value
			}

			$.ajax({
				url:		ACCMOB.common.currentPath + "/results?q=" + ACCMOB.productlisting.baseQuery + "&page=" + page,
				success: function(data) {
					$("ul#resultsList").append($.tmpl($("#resultsListItemsTemplate"), data)).listview('refresh');
					ACCMOB.productlisting.updatePaginationInfos(data.pagination);
					$.mobile.hidePageLoadingMsg(); //hide the page loader
					ACCMOB.productlisting.showMoreResultsArea.waypoint(ACCMOB.productlisting.infiniteScrollingConfig); // reconfigure waypoint eventhandler
				}
			});
		},

		updatePaginationInfos: function(paginationInfo) {
			ACCMOB.productlisting.currentPage = paginationInfo.currentPage;
			ACCMOB.productlisting.numberOfPages = paginationInfo.numberOfPages;
		},

		bindShowMoreResults: function(showMoreResultsArea) {
			this.showMoreResultsArea = showMoreResultsArea;
			ACCMOB.productlisting.showMoreResultsArea.live("click", function() {ACCMOB.productlisting.triggerLoadMoreResults()});
			ACCMOB.productlisting.showMoreResultsArea.waypoint(ACCMOB.productlisting.scrollingHandler, ACCMOB.productlisting.infiniteScrollingConfig);
		},

		bindSortingSelector: function() {
			$('#sort_form1').change(function() {
				this.submit();
			});
			$('#sort_form2').change(function() {
				this.submit();
			});
		}
};

$(document).ready(function() {
	with(ACCMOB.productlisting) {
		bindShowMoreResults($('#footer'));
		bindSortingSelector();
	}
});
