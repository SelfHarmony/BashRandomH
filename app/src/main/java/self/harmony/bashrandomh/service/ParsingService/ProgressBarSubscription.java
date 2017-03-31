package self.harmony.bashrandomh.service.ParsingService;

import rx.Observable;
import rx.subjects.BehaviorSubject;

//Created by selfharmony
public class ProgressBarSubscription {
    private static BehaviorSubject<Integer> mProgressBehaviourSubject = BehaviorSubject.create(1);


    public static BehaviorSubject<Integer> getProgressBehaviourSubject() {
        return mProgressBehaviourSubject;
    }

    public static Observable<Integer> getProgressObservable() {
        return mProgressBehaviourSubject.asObservable();
    }

}


