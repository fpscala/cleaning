function visible(t) {
    t = t[0];
    var e = t.getBoundingClientRect();
    return e.top >= 0 && e.left >= 0 && e.bottom <= (window.innerHeight || document.documentElement.clientHeight) && e.right <= (window.innerWidth || document.documentElement.clientWidth)
}

function Modal(t) {
    this.backdrop = t.backdrop, this.dismissers = t.dismissers, this.toggle = t.toggle, this.init()
}

function Tab(t) {
    this.tabs = t.tabs, this.tabContents = t.tabContents;
    var e = this;
    this.tabs.click(function(t) {
        t.preventDefault();
        var i = $(this);
        e.tabs.removeClass("price-list__tab--active"), i.addClass("price-list__tab--active"), e.tabContents.hide(), e.tabContents.eq(i.attr("data-tab-id")).show()
    })
}
var $document = $(document),
    $window = $(window),
    $body = $("body"),
    $navbar = $(".navbar");




$document.ready(function() {
    // Navbar lang dropdown

    var $dropdownToggle = $navbar.find('.navbar__item--drop-toggle');
    var $dropdown = $navbar.find('.navbar__dropdown');

    $dropdownToggle.find('a').first().click((e) => {
        e.preventDefault();
        $dropdown.addClass('navbar__dropdown--open');
    });

    $window.click(function(e){
        if ( !$(e.target).is($dropdownToggle.find('.navbar__link')) ) {
            $dropdown.removeClass('navbar__dropdown--open');
        }
    });


    $reviewsSection = $(".reviews"), $reviewsSection.find(".reviews__slider").slick({
        dots: !1,
        arrows: !0,
        infinite: !0,
        prevArrow: $reviewsSection.find(".arrow--prev-white"),
        nextArrow: $reviewsSection.find(".arrow--next-white")
    });
    var t = $(".navbar__nav"),
        e = !1;
    $(".hamburger").click(function(i) {
        i.preventDefault();
        var n = $(this);
        e || (e = !0, n.hasClass("hamburger--transformed") ? (n.removeClass("hamburger--transformed"), t.slideUp(200, function() {
            e = !1
        })) : (n.addClass("hamburger--transformed"), t.slideDown(200, function() {
            e = !1
        })))
    });
    var i, n = $("[data-surf-to]"),
        o = $("html, body");
    n.click(function(t) {
        i = $($(this).attr("data-surf-to")).offset().top, void 0 !== i && (t.preventDefault(), o.animate({
            scrollTop: i - $navbar.height() + "px"
        }));

        window.location.hash = $(this).attr("data-surf-to");
    })


}), Modal.prototype.show = function() {
    this.backdrop.addClass("backdrop--fade-in")
}, Modal.prototype.hide = function() {
    this.backdrop.removeClass("backdrop--fade-in")
}, Modal.prototype.init = function() {
    var t = this;
    this.dismissers.click(function(e) {
        e.preventDefault(), t.hide()
    }), this.toggle.click(function(e) {
        e.preventDefault(), t.show()
    })
}, Tab.prototype.showAll = function() {
    this.tabContents.fadeIn(200)
};

// Show hide review 

var $reviewsText = $('.reviews__comment');
var limit = 221;

$reviewsText.each(
    (index, item) => {
        var $this = $(item),
            $btn = $this.parent().find('.reviews__btn'),
            $cacheBtnHtml = $btn.html();

        if ($this.html().length >= limit) {


            $btn.show().click((e) => {
                e.preventDefault();

                if (!$this.hasClass('reviews__comment--expand')) {
                    $this.addClass('reviews__comment--expand');
                    $btn.html($btn.attr('data-min'));
                } else {
                    $this.removeClass('reviews__comment--expand');
                    $btn.html($cacheBtnHtml);
                }
            });

            $this.addClass('reviews__comment--minimize');
        } else {
            $btn.hide();
        }

    }
)

window.onload = function(){
    function scrollMe(selector)
    {
        window.location.hash = selector.substr(1);
        var section = $(window.location.hash);
        $('html, body').animate({scrollTop: section.offset().top - $navbar.height() + 'px'});
    }

    if (window.location.hash !== "") {
        scrollMe(window.location.hash);
    }
}



