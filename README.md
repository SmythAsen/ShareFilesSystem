# ShareFilesSystem
#### 这是我自己撸的一个简单的文件共享系统。
- 2017.08.02 第一步：客户端基础功能完成
- 2017.08.03 客户端进度条问题，用Java的SwingWoker类解决，java中不能再子线程中更新主线程界面中的ui，所以需要使用到委托机制。
>public abstract class SwingWorker<T,V>extends Objectimplements RunnableFuture<T>在专用线程中执行长时间 GUI 交互任务的抽象类。 

使用 Swing 编写多线程应用程序时，要记住两个约束条件：（有关详细信息，请参阅 How to Use Threads）： 

不应该在事件指派线程 上运行耗时任务。否则应用程序将无响应。 
只能在事件指派线程 上访问 Swing 组件。 

这些约束意味着需要时间密集计算操作的 GUI 应用程序至少需要以下两个线程：1) 执行长时间任务的线程； 2) 所有 GUI 相关活动的事件指派线程 （EDT）这涉及到难以实现的线程间通信。 

SwingWorker 设计用于需要在后台线程中运行长时间运行任务的情况，并可在完成后或者在处理过程中向 UI 提供更新。SwingWorker 的子类必须实现 doInBackground() 方法，以执行后台计算。 

工作流 

SwingWorker 的生命周期中包含三个线程： 

当前 线程：在此线程上调用 execute() 方法。它调度 SwingWorker 以在 worker 线程上执行并立即返回。可以使用 get 方法等待 SwingWorker 完成。 

Worker 线程：在此线程上调用 doInBackground() 方法。所有后台活动都应该在此线程上发生。要通知 PropertyChangeListeners 有关绑定 (bound) 属性的更改，请使用 firePropertyChange 和 getPropertyChangeSupport() 方法。默认情况下，有两个可用的绑定属性：state 和 progress。 

事件指派线程：所有与 Swing 有关的活动都在此线程上发生。SwingWorker 调用 process 和 done() 方法，并通知此线程的所有 PropertyChangeListener。 

通常，当前 线程就是事件指派线程。 

在 worker 线程上调用 doInBackground 方法之前，SwingWorker 通知所有 PropertyChangeListener 有关对 StateValue.STARTED 的 state 属性更改。doInBackground 方法完成后，执行 done 方法。然后 SwingWorker 通知所有 PropertyChangeListener 有关对 StateValue.DONE 的 state 属性更改。 

SwingWorker 被设计为只执行一次。多次执行 SwingWorker 将不会调用两次 doInBackground 方法。 

示例用法 

下例说明了最简单的使用范例：在后台完成某些处理，并在处理完成后更新 Swing 组件。 

假定想找到“Meaning of Life”并在 JLabel 中显示结果。 
``` java
final JLabel label;
class MeaningOfLifeFinder extends SwingWorker<String, Object> {
@Override
public String doInBackground() {
return findTheMeaningOfLife();
       }

@Override
protected void done() {
try { 
label.setText(get());
} catch (Exception ignore) {
           }
       }
   }
 
(new MeaningOfLifeFinder()).execute();
 在希望处理已经在事件指派线程 上准备好的数据时，下一个例子很有用。 

现在想要查找第一个 N 素数值并在 JTextArea 中显示结果。在计算过程中，想在 JProgressBar 中更新进度。最后，还要将该素数值打印到 System.out。 

class PrimeNumbersTask extends 
SwingWorker<List<Integer>, Integer> {
PrimeNumbersTask(JTextArea textArea, int numbersToFind) { 
//initialize 
     }

@Override
public List<Integer> doInBackground() {
while (! enough && ! isCancelled()) {
number = nextPrimeNumber();
publish(number);
setProgress(100 * numbers.size() / numbersToFind);
             }
         }
return numbers;
     }

@Override
protected void process(List<Integer> chunks) {
for (int number :chunks) {
textArea.append(number + "\n");
         }
     }
 }

JTextArea textArea = new JTextArea();
final JProgressBar progressBar = new JProgressBar(0, 100);
PrimeNumbersTask task = new PrimeNumbersTask(textArea, N);
task.addPropertyChangeListener(
new PropertyChangeListener() {
public  void propertyChange(PropertyChangeEvent evt) {
if ("progress".equals(evt.getPropertyName())) {
progressBar.setValue((Integer)evt.getNewValue());
             }
         }
     });

task.execute();
System.out.println(task.get()); //prints all prime numbers we have got
```
 因为 SwingWorker 实现了 Runnable，所以可以将 SwingWorker 提交给 Executor 执行。
 

