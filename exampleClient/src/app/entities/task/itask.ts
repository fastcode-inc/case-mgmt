export interface ITask {
  message?: string;
  status?: string;
  taskId: number;
  type?: string;
  username?: string;

  casesDescriptiveField?: number;
  caseId?: number;
}
