var responseData = [];

function fetchProducts() {
    $.ajax({
        url: "/bin/sortedProducts",
        type: 'GET',
        dataType: 'json',
        success: function(response) {
            console.log(response);
            responseData = response;
            renderCarousel(responseData); // Render carousel after data is fetched
        },
        error: function(xhr, status, error) {
            console.error('Error fetching products:', error);
        }
    });
}

fetchProducts();

var currentSlide = 0;

// Function to render carousel
function renderCarousel(data) {
    var carouselContainer = document.querySelector('.carousel-container');
    carouselContainer.innerHTML = '';

    data.forEach(function(item, index) {
        var slide = document.createElement('div');
        slide.classList.add('carousel-item');
        slide.innerHTML = '<img class="product-image" src="' + item.image + '" alt="Slide ' + (index + 1) + '">' +
                          '<p class="price">Price: $' + item.price + '</p>' +
                          '<p class="inventory">Inventory Number: ' + item.id + '</p>';
        carouselContainer.appendChild(slide);
    });
}

// Function to show previous slide
function prevSlide() {
    if (currentSlide > 0) {
        currentSlide--;
        showSlide();
    }
}

// Function to show next slide
function nextSlide() {
    if (currentSlide < responseData.length - 1) {
        currentSlide++;
        showSlide();
    }
}

// Function to display current slide
function showSlide() {
    var carouselContainer = document.querySelector('.carousel-container');
    var slideWidth = carouselContainer.offsetWidth;
    carouselContainer.style.transform = 'translateX(-' + (currentSlide * slideWidth) + 'px)';
}

// Function to filter carousel items
function filter(type) {
    if (type === 'high-low') {
        responseData.sort(function(a, b) {
            return b.price - a.price;
        });
    } else if (type === 'low-high') {
        responseData.sort(function(a, b) {
            return a.price - b.price;
        });
    } else if (type === 'recent') {
        responseData.sort(function(a, b) {
            return new Date(b.id) - new Date(a.id);
        });
    }
    renderCarousel(responseData);
}
