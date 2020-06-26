$(document).ready(function () {
    var baseUrl = "http://localhost:8080";
    //get pagination element
    var pagination = $('#pagination');
    //create a empty array for store the queries
    var queries = [];

//Initialize bootstrap 3 typeahead plugin
    $.ajax({
        method: "GET",
        url: baseUrl + "/words"
    }).success(function (data) {
        // the data is the resultlist of websites (for now)
        console.log("Received response " + data);
        $('#searchbox').typeahead(
            {
                name: "autocompletion extension",
                source: data
            });
    });

//listening searchbutton click active
    $("#searchbutton").click(function () {
        //Checking if the searchbox is empty to avoid sending empty String to the array/server
        if (""== $('#searchbox').val()) {
            return;
        }
        queries.push($('#searchbox').val());
        console.log("Sending request to server.");
        // send a request to server
        // url: relate to  @RequestMapping("/search") in Webapplication.java
        // data: get the value of inputed query word
        $.ajax({
            method: "GET",
            url: baseUrl + "/search",
            data: {query: $('#searchbox').val()}
        }).success(function (data) {
            // the data is the resultlist of websites (for now)
            console.log("Received response " + data.length);
            // hide the logo
            $("#logo").hide();
            // $("#responsesize").html("<p>" + data.length + " websites retrieved</p>");
            // re-init the website list
            $("#urllist").empty();
            // pagination.destroy();

            // re-init the pagination plugin
            pagination.empty();
            pagination.removeData("twbs-pagination");
            pagination.unbind('page');
            // if have websites
            if (data.length > 0) {
                // hide the message, do not show the number of outcome
                $("#responsesize").hide();
                // init pagination
                paginationInit(data);
            } else {
                // show the message, when get 0 websites.
                $("#responsesize").show();
                $("#responsesize").html("<p>" + data.length + " websites retrieved</p>");
            }
        });
    });

// Suitable for "enter" on the keyboard
// Listening the keypress active
    $("#searchbox").keypress(function (e) {
        if (e.which == 13) {
            $("#searchbutton").click();
        }
    });

// put the paginationInit function as a var
// data: results of websites
    paginationInit = function (data) {
        // The total number of websites
        var numberOfWebsites = data.length;
        // The number of websites that need to be show in pre page
        var numberOfPre = 8;
        // Calculating the totalpages
        //Returns the smallest integer greater than or equal to a given number.
        var totalPages = Math.ceil(numberOfWebsites / numberOfPre);

        pagination.twbsPagination({
            totalPages: totalPages,
            visiblePages: 10,
            startPage: 1,
            initiateStartPageClick: true,
            prev: '<',
            next: '>',
            // Listening when chick a page number
            onPageClick: function (event, page) {
                // console.info(page + ' (from pageClick)');
                // show the clicked page
                showPages(data, numberOfPre, page);
            }
        });
    };

// data: results of websites
// numberOfPre: The number of websites that need to be show in pre page
// currentPage: clicked page
    showPages = function (data, numberOfPre, currentPage) {
        // Calculating the start index in data that need to be show
        var startIndex = (currentPage - 1) * numberOfPre;
        // Calculating the end index in data that need to be show
        var endIndex = startIndex + numberOfPre;
        // for the last page
        if (endIndex > data.length) {
            endIndex = data.length;
        }

        // Create a dynamic ul
        var buffer = "<ul>\n";
        // foreach loop for the specific range of index from data
        // index: the index of data.slice(startIndex, endIndex)
        // value: the website{title:"",url:"",words:[]}
        $.each(data.slice(startIndex, endIndex), function (index, value) {
            // Create the title with link
            buffer += "<li><a href=\"" + value.url + "\">" + value.title + "</a>&nbsp;&nbsp;&nbsp;&nbsp;";
            // Create the url with link
            buffer += "<p style='margin-bottom: 1px''><a  href=\"" + value.url + "\" >" + value.url + "</a></p>";
            // Create the description from words
            buffer += "<p>";
            buffer += value.words[0].substring(0, 1).toUpperCase() + value.words[0].substring(1) + " ";
            $.each(value.words.slice(1, 50), function (index, value) {
                buffer += value + " ";
            });
            buffer += "...\n</p></li>"

        });
        buffer += "</ul>";
        // re-init the website list
        $("#urllist").empty();
        // add the dynamic ul
        $("#urllist").append(buffer);
    }
})
;

