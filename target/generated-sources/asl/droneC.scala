package asl
 import _root_.scala.collection.mutable.HashMap

 import _root_.akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
 import _root_.akka.actor.typed.scaladsl.{ActorContext, Behaviors, Routers}
 import java.util.logging.Logger
 import _root_.scala.util.Failure
 import _root_.scala.util.Success
 import _root_.akka.util.Timeout
 import _root_.scala.concurrent.duration._
 import _root_.akka.actor.typed.scaladsl.AskPattern._
 import _root_.scala.language.implicitConversions
 import _root_.scala.concurrent.{Await, Future}
 import _root_.scala.jdk.CollectionConverters._
 import std.converters._

 import scala.util.Random
 import bb._
 import infrastructure._
 import bb.expstyla.exp._
 import std.{AgentCommunicationLayer, DefaultCommunications}

 class droneC  (coms: AgentCommunicationLayer = new  DefaultCommunications,
                                     beliefBaseFactory: IBeliefBaseFactory = new StylaBeliefBaseFactory)
                      extends IntentionalAgentFactory {


 object Intention {

       def apply(p_executionContext: ExecutionContext): Behavior[ISubGoalMessage] = Behaviors.setup { context =>

         Behaviors.receive {
         (context, message) =>

          implicit val executionContext = p_executionContext.copy(intention = context, src = message.source)

         message match {
            case SubGoalMessage(_,_,r) =>
               message.goal match {

                   case droneC.this.adopt_achievement_start_0 =>
                     droneC.this.adopt_achievement_start_0.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_belief_antidronefire_2 =>
                     droneC.this.adopt_belief_antidronefire_2.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_achievement_radar_0 =>
                     droneC.this.adopt_achievement_radar_0.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_belief_targeting_3 =>
                     droneC.this.adopt_belief_targeting_3.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_achievement_moveup_0 =>
                     droneC.this.adopt_achievement_moveup_0.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_achievement_movedown_0 =>
                     droneC.this.adopt_achievement_movedown_0.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_achievement_moveleft_0 =>
                     droneC.this.adopt_achievement_moveleft_0.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_achievement_moveright_2 =>
                     droneC.this.adopt_achievement_moveright_2.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_achievement_whereIam_2 =>
                     droneC.this.adopt_achievement_whereIam_2.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_achievement_target_2 =>
                     droneC.this.adopt_achievement_target_2.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_belief_attacked_1 =>
                     droneC.this.adopt_belief_attacked_1.execute(message.params.asInstanceOf[Parameters])

                   case droneC.this.adopt_achievement_additionaltasks_0 =>
                     droneC.this.adopt_achievement_additionaltasks_0.execute(message.params.asInstanceOf[Parameters])


           case _ =>
             context.log.error("This actor can not handle goal of type {}", message.goal)
         }
           r match {
                 case a : AkkaMessageSource => 
                   a.src ! IntentionDoneMessage(AkkaMessageSource(executionContext.agent.self))
                 case DummyMessageSource(_) => 
                   context.log.error("Intention Done!")
               }

               Behaviors.same
             case InitEndMessage(r) =>
               Behaviors.stopped
       }
      }
     }
     }

 override def agentBuilder: Agent = new Agent()
 class Agent extends IAgent {

         override def agent_type: String = "droneC"

         var vars = VarMap()

         def initGoals()(implicit executionContext: ExecutionContext) = List[StructTerm](
         )

         def initBeliefs()(implicit executionContext: ExecutionContext) = List[StructTerm](
                     StructTerm(":-",Seq[GenericTerm](StructTerm("around",Seq[GenericTerm](vars("Z"),vars("T"))),StructTerm(",",Seq[GenericTerm](StructTerm(",",Seq[GenericTerm](StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y"))),StructTerm("==",Seq[GenericTerm](vars("X"),vars("Z"))))),StructTerm("==",Seq[GenericTerm](vars("Y"),vars("T")))))))
           ,
            StructTerm("initial",Seq[GenericTerm](IntTerm(4),IntTerm(1)))


         )

         def planApplicabilities()(implicit executionContext: ExecutionContext) = List[StructTerm](

                 )



      def apply(name: String, yellowPages: IYellowPages, MAS: ActorRef[IMessage], parent: IMessageSource): Behavior[IMessage] = {
           Behaviors.setup { context =>
             val yp: IYellowPages = yellowPages
             val bb: IBeliefBase[GenericTerm] = beliefBaseFactory()
             implicit val executionContext: ExecutionContext = ExecutionContext(
                            name = name,
                            agentType = agent_type,
                            agent = context,
                            yellowPages = yp,
                            beliefBase = bb,
                            logger = context.log,
                            goalParser = GoalParser,
                            parent = parent
                          )
             bb.assert(initBeliefs)
             bb.assert(planApplicabilities)

         val initiator = context.spawn(Intention(executionContext), "initiator")

         MAS ! ReadyMessage(context.self)
         Behaviors.receive {
           (context, message) =>
             message match {
               case StartMessage() =>


                 implicit val timeout: Timeout = 99999.seconds
                 implicit val ec = context.executionContext
                 implicit val scheduler = context.system.scheduler


                 //              initGoals.foreach( tuple => initiator ! SubGoalMessage(tuple._1,tuple._2,context.self))
                 initGoals.foreach(struct => {


                   val result: Future[IMessage] = initiator.ask[IMessage](ref => {
                     val subGoal = GoalParser.create_goal_message(struct, AkkaMessageSource(ref))
                     if (subGoal.isDefined)
                       subGoal.get
                     else
                       throw new RuntimeException(s"No plan for initial goal $struct")
                     }
                   )


                   //result.onComplete {
                   //  case Success(IntentionDoneMessage(r)) => IntentionDoneMessage(r)
                   //  case Failure(_) => IntentionErrorMessage(src = null)
                   //}

                   //Await.result(result, timeout.duration)

                   val res = Await.result(result, timeout.duration)

                   if(!res.isInstanceOf[IntentionDoneMessage]) {
                     throw new RuntimeException(s"Issue with initial goal $struct")
                     context.system.terminate()
                   }

                   //                context.ask[ISubGoalMessage, IMessage](initiator, ref => SubGoalMessage(tuple._1, tuple._2,name, ref)) {
                   //                  case Success(IntentionDoneMessage(_, _)) => IntentionDoneMessage()
                   //                  case Failure(_) => IntentionErrorMessage()
                   //                }
                 }
                 )

                 initiator ! InitEndMessage(context.self)
                 normal_behavior(MAS)

               //            case InitEndMessage(_) =>
               //              context.log.debug(f"$name: I have started, switching behavior")
               //              normal_behavior()
             }

         }
       }
     }

     def normal_behavior(MAS: ActorRef[IMessage])(implicit executionContext: ExecutionContext): Behavior[IMessage] = {
       Behaviors.setup { context =>

         val pool = Routers.pool(poolSize = 8)(
           Behaviors.supervise(Intention(executionContext)).onFailure[Exception](SupervisorStrategy.restart))

         val router = context.spawn(pool, "intention-pool")
         //MAS ! ReadyMessage(context.self)
         Behaviors.receive {
           (context, message) =>
             message match {
               case IntentionDoneMessage(s) =>
                 context.log.debug(f"${executionContext.name}: an intention was done by $s")
               case IntentionErrorMessage(c,s) =>
                 context.log.debug(f"${executionContext.name}: an intention was done by $s: $c")
               case SubGoalMessage(_, _, _) =>
                 router ! message.asInstanceOf[SubGoalMessage]
               case GoalMessage(m, ref) =>
                 m match {
                   case t: StructTerm =>
                     val subGoal = GoalParser.create_goal_message(t, ref)

                     if (subGoal.isDefined)
                       context.self ! subGoal.get
                     else
                       ref.asInstanceOf[AkkaMessageSource].src ! IntentionErrorMessage(NoPlanMessage(),AkkaMessageSource(executionContext.agent.self))


                 }

                case AskMessage(m, ref) =>
                                m match {
                                  case t: StructTerm =>
                                    val subGoal = GoalParser.create_test_message(t, ref)

                                    if (subGoal.isDefined)
                                      context.self ! subGoal.get
                                    else
                                      ref.asInstanceOf[AkkaMessageSource].src ! IntentionErrorMessage(NoPlanMessage(),AkkaMessageSource(executionContext.agent.self))
                                }
             case BeliefMessage(m, ref) =>
                  m match {
                    case t: StructTerm =>
                    if(executionContext.beliefBase.assertOne(t)) {
                      val subGoal = GoalParser.create_belief_message(t, ref)

                      if (subGoal.isDefined)
                        context.self ! subGoal.get
                    }
                  }

              case UnBeliefMessage(m, ref) =>
                   m match {
                     case t: StructTerm =>
                     if(executionContext.beliefBase.retractOne(t)) {
                       val subGoal = GoalParser.create_unbelief_message(t, ref)

                       if (subGoal.isDefined)
                         context.self ! subGoal.get
                     }
                   }
             }
             Behaviors.same
         }
       }
     }
   }



   object GoalParser extends IAgentGoalParser {
        override def create_goal_message(t: StructTerm, ref: IMessageSource) (implicit executionContext: ExecutionContext): Option[SubGoalMessage] = {
                                   if(t.matchOnlyFunctorAndArity("start",0)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_achievement_start_0, args, ref))
                                   } else   
                                   if(t.matchOnlyFunctorAndArity("radar",0)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_achievement_radar_0, args, ref))
                                   } else   
                                   if(t.matchOnlyFunctorAndArity("moveup",0)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_achievement_moveup_0, args, ref))
                                   } else  
                                   if(t.matchOnlyFunctorAndArity("movedown",0)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_achievement_movedown_0, args, ref))
                                   } else  
                                   if(t.matchOnlyFunctorAndArity("moveleft",0)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_achievement_moveleft_0, args, ref))
                                   } else  
                                   if(t.matchOnlyFunctorAndArity("moveright",2)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_achievement_moveright_2, args, ref))
                                   } else  
                                   if(t.matchOnlyFunctorAndArity("whereIam",2)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_achievement_whereIam_2, args, ref))
                                   } else  
                                   if(t.matchOnlyFunctorAndArity("target",2)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_achievement_target_2, args, ref))
                                   } else   
                                   if(t.matchOnlyFunctorAndArity("additionaltasks",0)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_achievement_additionaltasks_0, args, ref))
                                   } else   {
                    Option.empty[SubGoalMessage]
                    }

                }

        override def create_belief_message(t: StructTerm, ref: IMessageSource) (implicit executionContext: ExecutionContext): Option[SubGoalMessage] = {
                   
                                   if(t.matchOnlyFunctorAndArity("antidronefire",2)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_belief_antidronefire_2, args, ref))
                                   } else   
                                   if(t.matchOnlyFunctorAndArity("targeting",3)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_belief_targeting_3, args, ref))
                                   } else        
                                   if(t.matchOnlyFunctorAndArity("attacked",1)) {
                                     val args: Parameters = Parameters(t.terms.toList)
                                     Option(SubGoalMessage(adopt_belief_attacked_1, args, ref))
                                   } else    {
                    Option.empty[SubGoalMessage]
                    }

                }

         override def create_test_message(t: StructTerm, ref: IMessageSource) (implicit executionContext: ExecutionContext): Option[SubGoalMessage] = {
                                       {
                            Option.empty[SubGoalMessage]
                            }
                        }
          override def create_unbelief_message(t: StructTerm, ref: IMessageSource) (implicit executionContext: ExecutionContext): Option[SubGoalMessage] = {
                                                {
                                     Option.empty[SubGoalMessage]
                                     }
                                 }



        }


      object adopt_achievement_start_0 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         val r0 = executionContext.beliefBase.query(StructTerm("initial",Seq[GenericTerm](vars("X"),vars("Y"))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y")))),GoalParser)
                                          adopt_achievement_whereIam_2.execute(Parameters(List( vars("X") , vars("Y")  )))
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.inform(StructTerm("environment",Seq[GenericTerm]()),StructTerm("location",Seq[GenericTerm](vars("Self"),vars("X"),vars("Y"))))))
                                          adopt_achievement_moveup_0.execute(Parameters(List(  )))
                                          adopt_achievement_moveup_0.execute(Parameters(List(  )))
                                          adopt_achievement_radar_0.execute(Parameters(List(  )))
                                          adopt_achievement_moveup_0.execute(Parameters(List(  )))
                                          adopt_achievement_moveup_0.execute(Parameters(List(  )))
                                          adopt_achievement_moveup_0.execute(Parameters(List(  )))
                                          adopt_achievement_radar_0.execute(Parameters(List(  )))


                     }


      }

      object adopt_belief_antidronefire_2 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         vars +=(   "X" -> params.l_params(0))
                          vars +=(   "Y" -> params.l_params(1))

                         val r0 = executionContext.beliefBase.query(StructTerm("around",Seq[GenericTerm](vars("X"),vars("Y"))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("destroyed",Seq[GenericTerm](BooleanTerm(true)))),GoalParser)
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.inform(StructTerm("droneB",Seq[GenericTerm]()),StructTerm("attacked",Seq[GenericTerm](vars("Self"))))))
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.inform(StructTerm("droneA",Seq[GenericTerm]()),StructTerm("attacked",Seq[GenericTerm](vars("Self"))))))
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( ( (StringTerm("  drone ") + vars("Self"))  + StringTerm(" kaput ")) )))


                     }


      }

      object adopt_achievement_radar_0 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         val r0 = executionContext.beliefBase.query(StructTerm(",",Seq[GenericTerm](StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y"))),StructTerm("not",Seq[GenericTerm](StructTerm("destroyed",Seq[GenericTerm](BooleanTerm(true))))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( (vars("Self") + StringTerm(" radar on ")) )))
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.inform(StructTerm("fighter",Seq[GenericTerm]()),StructTerm("dronelocation",Seq[GenericTerm](vars("Self"),vars("X"),vars("Y"))))))
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.ask(StructTerm("environment",Seq[GenericTerm]()),StructTerm("targeting",Seq[GenericTerm](vars("Self"),vars("X"),vars("Y"))))))


                     }


      }

      object adopt_belief_targeting_3 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         vars +=(   "Self" -> params.l_params(0))
                          vars +=(   "X" -> params.l_params(1))
                          vars +=(   "Y" -> params.l_params(2))

                             plan0(vars)
                             return
                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          adopt_achievement_target_2.execute(Parameters(List( vars("X") , vars("Y")  )))


                     }


      }

      object adopt_achievement_moveup_0 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         val r0 = executionContext.beliefBase.query(StructTerm(",",Seq[GenericTerm](StructTerm(",",Seq[GenericTerm](StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y"))),StructTerm("is",Seq[GenericTerm](vars("Z"),StructTerm("+",Seq[GenericTerm](vars("Y"),IntTerm(1))))))),StructTerm("not",Seq[GenericTerm](StructTerm("destroyed",Seq[GenericTerm](BooleanTerm(true))))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( (vars("Self") + StringTerm(" moving up")) )))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y")))),GoalParser)
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("at",Seq[GenericTerm](vars("X"),vars("Z")))),GoalParser)
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.inform(StructTerm("environment",Seq[GenericTerm]()),StructTerm("location",Seq[GenericTerm](vars("Self"),vars("X"),vars("Z"))))))
                                          adopt_achievement_whereIam_2.execute(Parameters(List( vars("X") , vars("Z")  )))


                     }


      }

      object adopt_achievement_movedown_0 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         val r0 = executionContext.beliefBase.query(StructTerm(",",Seq[GenericTerm](StructTerm(",",Seq[GenericTerm](StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y"))),StructTerm("is",Seq[GenericTerm](vars("Z"),StructTerm("-",Seq[GenericTerm](vars("Y"),IntTerm(1))))))),StructTerm("not",Seq[GenericTerm](StructTerm("destroyed",Seq[GenericTerm](BooleanTerm(true))))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( (vars("Self") + StringTerm(" moving down")) )))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y")))),GoalParser)
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("at",Seq[GenericTerm](vars("X"),vars("Z")))),GoalParser)
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.inform(StructTerm("environment",Seq[GenericTerm]()),StructTerm("location",Seq[GenericTerm](vars("Self"),vars("X"),vars("Z"))))))
                                          adopt_achievement_whereIam_2.execute(Parameters(List( vars("X") , vars("Z")  )))


                     }


      }

      object adopt_achievement_moveleft_0 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         val r0 = executionContext.beliefBase.query(StructTerm(",",Seq[GenericTerm](StructTerm(",",Seq[GenericTerm](StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y"))),StructTerm("is",Seq[GenericTerm](vars("Z"),StructTerm("-",Seq[GenericTerm](vars("X"),IntTerm(1))))))),StructTerm("not",Seq[GenericTerm](StructTerm("destroyed",Seq[GenericTerm](BooleanTerm(true))))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( (vars("Self") + StringTerm(" moving left")) )))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y")))),GoalParser)
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("at",Seq[GenericTerm](vars("Z"),vars("X")))),GoalParser)
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.inform(StructTerm("environment",Seq[GenericTerm]()),StructTerm("location",Seq[GenericTerm](vars("Self"),vars("X"),vars("Z"))))))
                                          adopt_achievement_whereIam_2.execute(Parameters(List( vars("X") , vars("Z")  )))


                     }


      }

      object adopt_achievement_moveright_2 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         vars +=(   "X" -> params.l_params(0))
                          vars +=(   "Y" -> params.l_params(1))

                         val r0 = executionContext.beliefBase.query(StructTerm(",",Seq[GenericTerm](StructTerm(",",Seq[GenericTerm](StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y"))),StructTerm("is",Seq[GenericTerm](vars("Z"),StructTerm("+",Seq[GenericTerm](vars("X"),IntTerm(1))))))),StructTerm("not",Seq[GenericTerm](StructTerm("destroyed",Seq[GenericTerm](BooleanTerm(true))))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( (vars("Self") + StringTerm(" moving right")) )))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("-", StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y")))),GoalParser)
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("at",Seq[GenericTerm](vars("Z"),vars("X")))),GoalParser)
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.inform(StructTerm("environment",Seq[GenericTerm]()),StructTerm("location",Seq[GenericTerm](vars("Self"),vars("X"),vars("Z"))))))
                                          adopt_achievement_whereIam_2.execute(Parameters(List( vars("X") , vars("Z")  )))


                     }


      }

      object adopt_achievement_whereIam_2 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         vars +=(   "X" -> params.l_params(0))
                          vars +=(   "Y" -> params.l_params(1))

                         val r0 = executionContext.beliefBase.query(StructTerm(",",Seq[GenericTerm](StructTerm("at",Seq[GenericTerm](vars("X"),vars("Y"))),StructTerm("not",Seq[GenericTerm](StructTerm("destroyed",Seq[GenericTerm](BooleanTerm(true))))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( ( ( ( (vars("Self") + StringTerm(" is at "))  + vars("X"))  + StringTerm(", "))  + vars("Y")) )))


                     }


      }

      object adopt_achievement_target_2 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         vars +=(   "X" -> params.l_params(0))
                          vars +=(   "Y" -> params.l_params(1))

                         val r0 = executionContext.beliefBase.query(StructTerm("not",Seq[GenericTerm](StructTerm("destroyed",Seq[GenericTerm](BooleanTerm(true))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( ( ( (StringTerm("target is ") + vars("X"))  + StringTerm(", "))  + vars("Y")) )))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("targetposition",Seq[GenericTerm](vars("X"),vars("Y")))),GoalParser)
                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => coms.inform(StructTerm("artillery",Seq[GenericTerm]()),StructTerm("targetposition",Seq[GenericTerm](vars("X"),vars("Y"))))))


                     }


      }

      object adopt_belief_attacked_1 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         vars +=(   "X" -> params.l_params(0))

                         val r0 = executionContext.beliefBase.query(StructTerm("not",Seq[GenericTerm](StructTerm("destroyed",Seq[GenericTerm](BooleanTerm(true))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( ( (vars("Self") + StringTerm(" received info about destruction of "))  + vars("X")) )))
                                           BeliefUpdateAction.execute(BeliefUpdateAction.Parameters("+", StructTerm("problems",Seq[GenericTerm](vars("X")))),GoalParser)


                     }


      }

      object adopt_achievement_additionaltasks_0 extends IGoal {

        def execute(params: Parameters) (implicit executionContext: ExecutionContext) : Unit = {
         var vars = VarMap()

         vars("Self").bind_to(StringTerm(executionContext.name))
         vars("Source").bind_to(StringTerm(executionContext.src.name))
         vars("Parent").bind_to(StringTerm(executionContext.parent.name))






                 //plan 0 start

                         vars.clear()
                         vars("Self").bind_to(StringTerm(executionContext.name))
                         vars("Source").bind_to(StringTerm(executionContext.src.name))
                         vars("Parent").bind_to(StringTerm(executionContext.parent.name))
                         val r0 = executionContext.beliefBase.query(StructTerm(",",Seq[GenericTerm](StructTerm("not",Seq[GenericTerm](StructTerm("destroyed",Seq[GenericTerm](BooleanTerm(true))))),StructTerm("problems",Seq[GenericTerm](StructTerm("droneA",Seq[GenericTerm]()))))))

                         if (r0.result) {
                             r0.bindings foreach { case (k, v) =>
                            // vars += (k -> v.asInstanceOf[GenericTerm])
                                      vars(k).bind_to(v)
                             }
                             plan0(vars)
                             return
                          }

                          // plan 0 end


             executionContext.src.asInstanceOf[AkkaMessageSource].address() ! IntentionErrorMessage(NoApplicablePlanMessage(),AkkaMessageSource(executionContext.agent.self))

        }


                      def plan0(vars: VarMap)(implicit executionContext: ExecutionContext): Unit = {

                                          PrimitiveAction.execute(PrimitiveAction.Parameters(() => println( (StructTerm("self",Seq[GenericTerm]()) + StringTerm(" is going to replace droneA ")) )))
                                          adopt_achievement_movedown_0.execute(Parameters(List(  )))
                                          adopt_achievement_movedown_0.execute(Parameters(List(  )))
                                          adopt_achievement_moveleft_0.execute(Parameters(List(  )))
                                          adopt_achievement_radar_0.execute(Parameters(List(  )))
                                          adopt_achievement_movedown_0.execute(Parameters(List(  )))


                     }


      }





 }
object droneC_companion { 
   def create() = new droneC().agentBuilder 
   def create(in_coms : AgentCommunicationLayer) = new droneC(coms = in_coms).agentBuilder 
   def create(in_beliefBaseFactory: IBeliefBaseFactory) = new droneC(beliefBaseFactory = in_beliefBaseFactory).agentBuilder 
   def create(in_coms : AgentCommunicationLayer, in_beliefBaseFactory: IBeliefBaseFactory) = new droneC(coms = in_coms, beliefBaseFactory = in_beliefBaseFactory).agentBuilder 
} 
