package cn.tripman.grow;

public class FindTwoArray {

    public static void main(String[] args) {
        System.out.println(findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));
    }

    /**
     * 二分查找
     * <p>
     * 数组分别是m n
     * <p>
     * 让分位线左边多一个元素
     * <p>
     * 分割方法 左边大于右边一个或者相等
     * <p>
     * 左边所有的数值小于等于右边的数值
     * <p>
     * （1）当为偶数： 分割线左边的最大值+ 分割线右边的最小值 /2
     * （2）当为奇数： 分割线左边的最大值
     * <p>
     * (1) size left =（m+n）/2
     * (2) size left = (m+n+1)/2
     * <p>
     * 计算机对除法的处理可以统一(1)(2)的结果 size left = （m+n+1）/2
     * <p>
     * i,j 代表分割的大的两块结果 有 i+j = （m+n+1）/2
     * <p>
     * 性质是交叉比大小
     * <p>
     * 有 num1[i-1] <= num2[j] 且 num2[j] >= num1[i-1]
     */
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        int m = nums1.length;
        int n = nums2.length;
        int totalLeft = (m + n + 1) / 2;

        int left = 0;
        int right = m;
        while (left < right) {
            int i = left + (right - left + 1) / 2;
            int j = totalLeft - i;
            if (nums1[i - 1] > nums2[j]) {
                right = i - 1;
            } else {
                left = i;
            }
        }
        int i = left;
        int j = totalLeft - i;

        int num1LeftMax = i == 0 ? Integer.MIN_VALUE : nums1[i - 1];
        int num1RightMin = i == m ? Integer.MAX_VALUE : nums1[i];

        int num2LeftMax = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
        int num2RightMin = j == n ? Integer.MAX_VALUE : nums2[j];

        if ((m + n) % 2 == 1) {
            return Math.max(num1LeftMax, num2LeftMax);
        }
        return (double) (Math.max(num1LeftMax, num2LeftMax) + Math.min(num1RightMin, num2RightMin)) / 2d;
    }


}
