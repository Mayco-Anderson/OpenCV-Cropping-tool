# OpenCV Cropping tool
I created this tool to make it easier the process of marking several images. It can be really useful when you're working with OpenCV and you want to create your own object detector.
![](https://github.com/Mayco-Anderson/OpenCV-Cropping-tool/blob/master/ImageCropping/images/pv4.png)
First you need to select a image source folder.
![](https://github.com/Mayco-Anderson/OpenCV-Cropping-tool/blob/master/ImageCropping/images/pv2.png)
Than you must select a text file which the program will use to output data.<br>
**Note** You can also use a file that already have some data about your images. The program will load the data and also display the marked areas of the corresponding image. Therefore you can also use this program just to view the marked areas of your images.
![](https://github.com/Mayco-Anderson/OpenCV-Cropping-tool/blob/master/ImageCropping/images/pv3.png)
In the left there is a list of all the images and files located inside the source folder you selected.<br>
When you select an image, the program will first try to load the marked area corresponding to this image.<br>
Than you can simply click and drag the mouse on top of this image to drawn a square on it.<br>
**Note** The square is not actually drawn in the image. It just represents the marked area. The image is **never** modified.<br>
When you hit the Button Save, all the marked areas of all images are stored in the text file that you provided.<br>
Data are stored in the following format:<br>
image_Test.png 1 42.0 43.0 69.0 67.0<br>
Dara are separated by spaces. The first argument is the name of the image, than the number of objects in this image (right now this program can only handle 1 object per image), than there is the X, Y, Width and Height of the marked area.<br><br>
Hope this program become useful to you!<br>
I will be working on some upgrades of this software as soon as I need it.<br>
