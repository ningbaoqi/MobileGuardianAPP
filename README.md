### 导航界面功能


+ android 7.0行为变更中系统权限的更改里面提到：传递软件包往域外的 file:// URI 可能给接收器留下无法访问的路径。因此，尝试传递 file:// URI 会触发 FileUriExposedException。分享私有文件内容的推荐方法是使用 FileProvider；

