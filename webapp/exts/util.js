/**
 * 日期format
 * @param format
 * @returns {*}
 */
Date.prototype.format = function (format) {
    var  o = {
        "M+" : this.getMonth() + 1,//month
        "d+" : this.getDate(),//day
        "h+" : this.getHours(),//hour
        "m+" : this.getMinutes(),//minute
        "s+" : this.getSeconds(),//second
        "q+" : Math.floor((this.getMonth() + 3) / 3),//quarter
        "S+" : this.getMilliseconds()//millisecond
    }
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,(this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if(new RegExp("(" + k + ")").test(format))format = format.replace(RegExp.$1,RegExp.$1.length == 1 ? o[k] :
        ("00" + o[k]).substr(("" + o[k]).length));
    return format;
};

//限制输入字符个数
(function ($) {
    $.fn.extend({
        limiter: function (limit) {
            $(this).on("keyup focus", function () {
                setCount(this);
            });
            function setCount(src) {
                var chars = src.value.length;
                if (chars > limit) {
                    src.value = src.value.substr(0, limit);
                    chars = limit;
                }
            }
            setCount($(this)[0]);
        }
    });
})(jQuery);

//判断数组中是否有重复元素
function isRepeat(arr) {
    var hash = {};
    for (var i in arr) {
        if (hash[arr[i]])
            return true;
        hash[arr[i]] = true;
    }
    return false;
}
