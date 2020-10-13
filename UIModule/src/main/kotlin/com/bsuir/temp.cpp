#pragma hdrstop
#pragma argsused
#include <tchar.h>
#include <stdio.h>

/* функции загрузки файлов**********************************************/
 //для установки консоль на паузу
#include <conio.h>
//вывод данных в консоль
#include <iostream.h>
//функции работы с файлом
#include <fstream.h>
//функции работы с файлом
#include <sstream>
//функции работы со строками
#include <string.h>
#include <cstring>

//переменная для чтения строк из файла
 string line;
//переменная ссылка на читаемый файл файл
 fstream My_File_obj;
//точки из которых будет состоять обьект
float* coords;
int* Index;

int  coords_index=0, Fase_index=0; //номер текущей прочитанной коордианты точки

struct coord
{
   float x;
   float y;
   float z;
};
struct polygons
{
   coord* point; //1 точки
};
//точка
coord* MyMass;
//полигоны
polygons* models;
//количества точек
 int point=1;
//собераем координаты в индексы
 int p=0;
//собираем обьект в единую структуру
void Compare(float* Coords, int* indexs, int count_fase, int count_point)
{
//задаем массив для точек
  MyMass = new  coord[count_point/3];
  //(coord*) malloc (count_point/3);
//задаем массив для полигонов
  models = new polygons[count_fase/4];
  //(polygons*) malloc (count_fase/4);

//перебираем точки и присваиваем координаты каждой с индеса 1
  for (int i = 0; i < count_point; i+=3)
  {
    MyMass[point].x=Coords[i+0]; //координата Х
    MyMass[point].y=Coords[i+1]; //координата Y
    MyMass[point].z=Coords[i+2]; //координата Z
   point++;
  }
   p=0;
   //перебираем ребра и присваиваем координаты в зависимости от индекса точки
   for (int f = 0; f < count_fase; f+=4)
   {
    models[p].point=new coord[4];
        for (int s = 0; s < 4; s++)
          models[p].point[s]= MyMass[indexs[f+s]];

 /*
  models[p].point[1]= MyMass[indexs[f+1]];
  models[p].point[2]= MyMass[indexs[f+2]];
  models[p].point[3]= MyMass[indexs[f+3]];     */
    p++;
   }

}
//читаем файл
void Rad_file_obj(char* name_file)
{
My_File_obj.open(name_file);
//открываем файл
   if (My_File_obj.is_open())
   { //если открыли будем читать данные из файла
     cout << "file open";
     //перечитсывем файл до концы
    My_File_obj.seekg (0, ios::end);
    //получаем количество считанных из файла символов
    int  File_Size = My_File_obj.tellg();
     //задаем размер временного массива для координат
    float*   Tmp_Coords = (float*) malloc (File_Size);
     //задаем размер временного массива индексов точек поверхности
    int*     Tmp_faseArray=(int*) malloc (File_Size);
    //устанавливаем в 0 элемент массива
    coords_index=0;
    Fase_index=0;
    //устанавливаем курсор в начало файла
    My_File_obj.seekg (0, ios::beg);

    int countpoint=0;
    int countfase=0;
     //читаем файл до конца
     while (!My_File_obj.eof())
        {
            //читаем строку текста из файла
             getline (My_File_obj,line);
             //проверяем первые два символа строки
             if ((line.c_str()[0] == 'v')&&(line.c_str()[1]==' '))
             {
                //обнуляем 1 символ
                    line[0] = ' ';
                //обнуляем 1 символ
                    line[1] = ' ';
                //чтобы функция правельно разбирала координаты обнуляем начало строки
                sscanf(line.c_str(),"%f %f %f ",
                                &Tmp_Coords[coords_index+0],   //координата X
                                &Tmp_Coords[coords_index+1], //координата Y
                                &Tmp_Coords[coords_index+2]  //координата Z
                       );



               //сдвигаем индекс на 3 так как 3 координаты
               coords_index+=3;
                //расчет точек
               countpoint++;
             }
        //проверяем не нашли ли грань
           if ((line.c_str()[0] == 'f')&&(line.c_str()[1]==' '))
           {
            //обнуляем 1 символ
                line[0] = ' ';
            //получаем параметры поверхности
            //     v1       v2     v3      v3
             int tmp_point[4], tmp_normal[4], tmp_texture[4];
              sscanf(line.c_str(),"%i/%i/%i%i/%i/%i%i/%i/%i%i/%i/%i",
              // номер точки  номер нормали   номер текстуры
                &tmp_point[0],&tmp_normal[0], &tmp_texture[0]  //p1
               ,&tmp_point[1],&tmp_normal[1], &tmp_texture[1]  //p2
               ,&tmp_point[2],&tmp_normal[2], &tmp_texture[2]  //p3
               ,&tmp_point[3],&tmp_normal[3], &tmp_texture[3]  //p4
              );

             //сохраняем индексы в массив
              Tmp_faseArray[Fase_index+0]=tmp_point[0]; //сохраняем первый индекс точки
              Tmp_faseArray[Fase_index+1]=tmp_point[1]; //сохраняем второй индекс точки
              Tmp_faseArray[Fase_index+2]=tmp_point[2]; //сохраняем третий индекс точки
              Tmp_faseArray[Fase_index+3]=tmp_point[3]; //сохраняем четвертый индекс точки



             Fase_index+=4;
             countfase++;
           }
        }

      //определяем параметры полигонов для рисования
      Compare(Tmp_Coords,Tmp_faseArray,Fase_index,coords_index );
  }
  else
   {
   //если не открыли не будем читать
     cout << "file not open";
   }


}
/*реализация OpenGL********************************************************/
#include  "glut.h"
#include <windows.h>
 GLfloat  tx=0;            // Сдвиг по оси X
GLfloat     ty=0;            // Y
GLfloat     tz=0;            // Z
GLfloat  rx=0;            // Угол поворта сцены вокруг оси X
GLfloat  ry=0;            // Y
GLint     tt=0;            // Активная плоскось: 0 - XY, 1 - XZ

int mx=0,my=0;                // Координаты мыши
bool ldown=false,        // Нажата левая клавиша мыши?
     rdown =false;


float size=1;

void Draw_Obj()
{
   /*
       glEnable(GL_ALPHA_TEST);
  glEnable(GL_BLEND);
  glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
   glEnable(GL_LIGHTING);
    glLightModelf(GL_LIGHT_MODEL_TWO_SIDE, GL_TRUE);
    glEnable(GL_NORMALIZE);
    float light0_diffuse[] = {1, 1, 1};
    float light0_direction[] = {-1, -1, 0, 100};
    glEnable(GL_LIGHT0);
    glLightfv(GL_LIGHT0, GL_DIFFUSE, light0_diffuse);
    glLightfv(GL_LIGHT0, GL_POSITION, light0_direction);
                                                         */
    for (int  i = 0; i < p; i++)
    {
    //рисуем каркас из линий
    //GL_POLYGON  GL_POINTS   GL_LINES GL_POLYGON GL_QUADS
        glColor3f(0,0,0);
        glBegin(GL_LINE_LOOP);
        for (int s=0; s<4; s++)
        glVertex3f(models[i].point[s].x,models[i].point[s].y,models[i].point[s].z);
        glEnd();
     //рисуем полигоны
        glColor3f(0,1,0);
        glBegin(GL_POLYGON);
        for (int s=0; s<4; s++)
        glVertex3f(models[i].point[s].x,models[i].point[s].y,models[i].point[s].z);
        glEnd();
    }


}

void MouseMotion(int x, int y)    //Перемешение мыши
{
    if (ldown)        // Левая кнопка
    {
        rx+=0.5*(y-my);    //Изменение угола поворота
        ry+=0.5*(x-mx);
        mx=x;
        my=y;
       //    glutPostRedisplay();    //Перерисовать экран
    }

    if (rdown)    //Правая
    {
        tx+=0.01*(x-mx);    //Перемещение вдоль активной плоскости
        if (tt)
            tz+=0.01*(y-my);
        else
            ty+=0.01*(my-y);
        mx=x;
        my=y;
    }
        cout <<"\n X="<< tx<< " Y= " << ty<<" Z="<< tz ;
}


void Mouse(int button, int state, int x, int y)        //Обработка щелчков мыши
{
    cout << "\nbutton "<< button << "state" << state;

    if (button==GLUT_LEFT_BUTTON)        //Левая кнопка
    {
        switch (state)
        {
            case GLUT_DOWN:        //Если нажата
                ldown=true;        //установить флаг
                mx=x;            //Запомнить координаты
                my=y;
                break;
            case GLUT_UP:
                ldown=false;
                break;
        }
    }
    if (button==GLUT_RIGHT_BUTTON)    //Правая кнопка
    {
        switch (state)
        {
            case GLUT_DOWN:
                rdown=true;
                mx=x;
                my=y;
                break;
            case GLUT_UP:
                rdown=false;
                break;
        }
    }


}


void Display()
{
   glEnable(GL_DEPTH_TEST);
    glEnable(GL_LIGHTING);
    glEnable(GL_LIGHT0);
    glEnable(GL_COLOR_MATERIAL);

    glColor3f(0.1,0.7,0.2);
    glClearColor(0.5, 0.5, 0.75, 1);

 //    glClearColor(0.5f, 0.5f, 0.5f, 1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);



   glPushMatrix();
   glTranslatef(tx,ty,tz);        //Перемещение и поворт объекта
        glRotatef(rx,1,0,0);
        glRotatef(ry,0,1,0);
        glScalef(size,size,size);        //Перемещение и поворт объекта
         Draw_Obj();            //Вывод объекта на экран
  glPopMatrix();
  glutSwapBuffers();
}

void Keyboard(unsigned char key,int x,int y)            //Обработка сообщений от клавиатуры
{
    switch (key)
    {
        case VK_ESCAPE:        //Если нажата клавиша ESC - выход
            exit(0);
        break;
            case '+':        //Если нажата клавиша ESC - выход
             size+=0.1f;
        break;
        case '-':        //Если нажата клавиша ESC - выход
             size-=0.1f;
        break;
    }
   if (size<0) size=0;
}
 void IDLE()
{
glutPostRedisplay();
}
void InitOpenGL(int argc, _TCHAR* argv[])
{


   //включаем потдержку OpenGL в приложении
    glutInit(&argc, argv);
//задаем параметры рисования картинки
    glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH);
//устанавливаем размеры окна для картинки
    glutInitWindowSize(640, 480);
//устанавливаем позицию окна
    glutInitWindowPosition(100, 100);
//создаем окно и указываем ему заголовок
    glutCreateWindow("My first Pack Man");
//вызываем функцию рисования окна
    glutDisplayFunc(Display);
//обработка нажатия клавиш
    glutKeyboardFunc(Keyboard);

    glutMouseFunc(Mouse);

    glutMotionFunc(MouseMotion);

    glutIdleFunc(IDLE);

//запускаем бесконечный цикл работы приложения
    glutMainLoop();
}

/**************************************************************************/
int _tmain(int argc, _TCHAR* argv[])
{
//загружаем файл
cout <<"Loading cube2.obj";
  Rad_file_obj("cube2.obj");
//загружаем граяический примитив с список отображения

//запускаем OpenGL
   InitOpenGL(argc, argv);
    return 0;
}
