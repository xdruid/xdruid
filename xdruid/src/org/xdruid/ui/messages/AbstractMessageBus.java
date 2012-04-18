package org.xdruid.ui.messages;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;

public class AbstractMessageBus implements MessageBus {

	private static int _sequence = 0;

	private Map<String, Map<String, Map<Object, Registration>>> registered = new HashMap<String, Map<String, Map<Object, Registration>>>();

	private Map<String, Registration> perId = new HashMap<String, AbstractMessageBus.Registration>();

	private String getId(String topic, String callback) {
		return topic + "-" + callback + "-" + _sequence++;
	}

	public void publish(String topic, Message message) throws Exception{
		Map<String, Map<Object, Registration>> allInTopic = registered
				.get(topic);
		if (allInTopic != null) {
			for (Map.Entry<String, Map<Object, Registration>> e : allInTopic
					.entrySet()) {
				for (Map.Entry<Object, Registration> re : e.getValue()
						.entrySet()) {
					if (re.getValue().canAccept(message)) {
						publish(topic, message, re.getValue());
					}
				}
			}
		}

	}
	
	private void publish(String topic, Message message, Registration registration) throws  Exception{
		if(registration.async){
			AsyncDispatch ad = new AsyncDispatch(registration, topic);
			ad.execute(message);
		}else{
			registration.dispatch(topic, message);
		}
	}

	public String subscribe(String topic, String callback, Object target,
			Object source, boolean async) throws Exception {
		String id = getId(topic, callback);

		Map<String, Map<Object, Registration>> perTopic = registered.get(topic);
		if (perTopic == null) {
			perTopic = new HashMap<String, Map<Object, Registration>>();
			registered.put(topic, perTopic);
		}

		Map<Object, Registration> perCallback = perTopic.get(callback);
		if (perCallback == null) {
			perCallback = new HashMap<Object, Registration>();
			perTopic.put(callback, perCallback);
		}

		Registration registration = perCallback.get(target);
		if (registration != null) {
			return registration.id;
		}

		registration = new Registration(id, callback, target, source, async);
		perCallback.put(target, registration);
		perId.put(id, registration);
		return id;
	}

	public int unsubscribe(String topic, String id, boolean all) {
		if(!all){
			
		}
		return 0;
	}

	private class Registration {
		public String id;
		public Object target;
		public Object source;
		public boolean async;

		private Method methodCallback;

		public Registration(String id, String callback,
				Object target, Object source, boolean async) throws SecurityException,
				NoSuchMethodException {
			this.id = id;
			this.target = target;
			this.source = source;
			this.async = async;
			
			methodCallback = target.getClass().getMethod(callback,
					String.class, Message.class);

		}

		public void dispatch(String topic, Message message)
				throws IllegalArgumentException, IllegalAccessException,
				InvocationTargetException {
			if (methodCallback != null) {
				methodCallback.invoke(target, topic, message);
			}
		}

		public boolean canAccept(Message message) {
			boolean accept = true;
			if(source != null && target != null){
				return source.equals(message.source) &&
						target.equals(message.target);
			}else if(source != null && message.source != null){
				return source.equals(message.source);
			}else if(target != null && message.target != null){
				return target.equals(message.target);
			}
			return accept;
		}

	}
	
	

	private class AsyncDispatch extends AsyncTask<Message, Integer, Exception>{

		private Registration registration;
		private String topic;
		
		
		
		public AsyncDispatch(Registration registration, String topic) {
			super();
			this.registration = registration;
			this.topic = topic;
		}

		@Override
		protected Exception doInBackground(Message... params) {
			try {
				registration.dispatch(topic, params[0]);
			} catch (IllegalArgumentException e) {
				return e;
			} catch (IllegalAccessException e) {
				return e;
			} catch (InvocationTargetException e) {
				return e;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Exception result) {
			if(result != null){
				throw new RuntimeException(result);
			}
		}
		
	}
}
