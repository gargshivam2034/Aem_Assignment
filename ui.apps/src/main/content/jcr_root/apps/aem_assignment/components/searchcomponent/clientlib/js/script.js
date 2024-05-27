
console.log(21321)

$(document).ready(function() {
    $('#searchButton').click(function() {
        var query = $('#productSearchInput').val();
        if (query.length > 3) {
            searchProducts(query);
        }
        else{
            clearResults();
        }
    });

    $('#productSearchInput').on('input', function() {
        var query = $(this).val();
        if (query.length > 3) {
            console.log(query);
            searchProducts(query);
        }
        else{
            clearResults();
        }
    });

    function searchProducts(query) {
        $.ajax({
            url: '/bin/productsearch',
            method: 'GET',
            data: { query: query },
            success: function(response) {
                console.log(response);
                displayResults(response);
            },
            error: function(xhr, status, error) {
                console.error('Error fetching search results:', error);
            }
        });
    }

    function displayResults(results) {
        var $resultsList = $('#productSearchResults');
        $resultsList.empty();
        results.forEach(function(product) {
            $resultsList.append('<li>' + product + '</li>');
        });
    }

    function clearResults() {
        $('#productSearchResults').empty();
    }
});
