import React, { useEffect, useState } from "react";
import AppLayout from "../../layout/AppLayout";
import { useNavigate } from "react-router-dom";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import { useLocation } from "react-router-dom";

// import Swal from "sweetalert2";
// import axios from "axios";
// import { v4 as uuidv4 } from "uuid";
// "http://localhost:8485/AccountManagementAPI/accountClosure/getdataforclosingaccount";
//  {
//   "strAccountNumber": "11100000000008",
//   "strCustId": "23000003126"
//   }

// "http://localhost:8485/AccountManagementAPI/accountClosure/makerprocess";

// {
//                "strAccountNumber": "11100000000007",
//                "requestStatus":"Approved"
//     }
// 	{
//  "strAccountNumber": "11100000000007",
//               "requestStatus":"reject"
// "reasonForRejection":"User Want To Reject"

// }
export default function AccountClosureRequest() {
  // const { accountHolderName, strCustId } = props.location.state;
  const location = useLocation();
  const { accountHolderName, strCustId, strAccountNumber } = location.state;

  const accessToken = localStorage.getItem("token");
  console.log("abc" + accessToken);
  const navigate = useNavigate();
  const [approverejectAlldata, setapproverejectAlldata] = useState({});
  const [tableAlldata, settableAlldata] = useState([]);
  let AccountType = tableAlldata.strAccountType;

  let tier = approverejectAlldata.currentAccountTier;
  let Addresdoctype = approverejectAlldata.strAddressProofDocumentId;
  let Addresdocvalue = approverejectAlldata.strAddressProofDocumentValue;
  let Addressimg = approverejectAlldata.strTier1PassportPhotograph;
  let Identitydoctype = approverejectAlldata.strIdentityProofDocumentId;
  let Identitydocvalue = approverejectAlldata.strIdentityProofDocumentValue;
  let IdentityProofImage = approverejectAlldata.strIdentityProofDocumentId;
  let resclousing = approverejectAlldata.closureReason;
  let makerid = approverejectAlldata.makerUserId;
  let closinblc = approverejectAlldata.currentAccountBalance;
  // let accounttype = approverejectAlldata.strAccountType;
  let AccountNumber = approverejectAlldata.strAccountNumber;
  let requestStatus = approverejectAlldata.requestStatus;
  let transferAmount = approverejectAlldata.transferAmount;
  // const [strCustId, setCustId] = useState("");
  // const [strAccountNumber, setAccountNumber] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [checkedHnadler, setCheckedHnadler] = useState(false);

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };
  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };
  const handleCloseAlert = () => {
    setShowAlert(false);
  };
  useEffect(() => {
    approveReject();
  }, []);
  const approveReject = async () => {
    setCheckedHnadler(false);
    try {
      const response = await amsApi.post(
        `accountclouser/getdataforclosingaccount`,
        {
          strCustId: strCustId,
          strAccountNumber: strAccountNumber,
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      if (response.data.code === "S0000") {
        setapproverejectAlldata(response.data.listOfAccountClouser);
        // setCheckpoint(1);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };

  const approve = async () => {
    try {
      const response = await amsApi.post(
        `accountClosure/makerprocess`,
        // `journalTransfer/rejectJornalTransfer`,
        {
          strAccountNumber: strAccountNumber,
          requestStatus: "Approved",
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      if (response.data.code === "S0000") {
        handleShowSuccess(response.data.message);
        // navigate(-1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  const reject = async () => {
    try {
      const response = await amsApi.post(
        `accountClosure/makerprocess`,
        {
          strAccountNumber: strAccountNumber,
          requestStatus: "reject",
          reasonForRejection: resclousing,
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      if (response.data.code === "S0000") {
        handleShowSuccess(response.data.message);
        // navigate(-1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  useEffect(() => {
    dataTable();
  }, []);
  const dataTable = async () => {
    setCheckedHnadler(false);
    try {
      const response = await amsApi.post(
        `accountclouser/getLinkedAccount`,
        {
          strCustId: strCustId,
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      if (response.data.code === "S0000") {
        settableAlldata(response.data.accountList);
        // setCheckpoint(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full scale-96">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-sm  sm:text-lg text-black :text-2xl  leading-normal ">
                Account Close Request
              </p>
            </div>
          </div>
        </div>
        <div className="max-w-full mx-auto h-full overflow-auto max-h-full lg:max-h-[28rem]">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4  sm:p-2 bg-white  ">
                <div className="grid gap-6 mb-2 md:grid-cols-4 px-2">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Customer Name <span className="text-red-600 ">*</span>
                    </label>
                    <input
                      type="text"
                      id="accountHolderName"
                      disabled
                      defaultValue={accountHolderName}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="accountHolderName"
                      // required
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Customer ID <span className="text-red-600 ">*</span>
                    </label>
                    <input
                      type="text"
                      id="strCustId"
                      disabled
                      defaultValue={strCustId}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Customer ID"
                      // required
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Current Tier <span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="tier"
                      disabled
                      defaultValue={tier}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Current Tier"
                      // required
                    />
                  </div>
                </div>

                <hr className=" bg-gray-800 border-0  dark:bg-gray-700 px-8" />
                <div>
                  <div>
                    {""}
                    <h2 className="font-normal xs:font-bold  px-2">
                      Address Proof(POA)
                    </h2>
                  </div>
                  <div className="grid gap-6 mt-2 md:grid-cols-3 px-3 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Document Type <span className="text-red-600 ">*</span>
                      </label>
                      <input
                        type="text"
                        id="Document Type"
                        disabled
                        defaultValue={Addresdoctype}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Document Type"
                        // required
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Document Value{""}
                        <span className="text-red-600 ">*</span>
                      </label>
                      <input
                        type="text"
                        id="phone"
                        disabled
                        defaultValue={Addresdocvalue}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Document Value"
                        // required
                      />
                    </div>

                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Document Image{""}
                        <span className="text-red-600 ">*</span>
                      </label>
                      <input
                        type="text"
                        id="documentimage"
                        disabled
                        defaultValue={Addressimg}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Document Image"
                        // required
                      />
                    </div>
                  </div>

                  <div>
                    {""}
                    <h2 className="font-normal mt-2 xs:font-bold px-3 py-1">
                      Identity Proof (POI)
                    </h2>
                  </div>
                  <div className="grid gap-6  md:grid-cols-3 px-3 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Document Type <span className="text-red-600 ">*</span>
                      </label>
                      <input
                        type="text"
                        id="document"
                        disabled
                        defaultValue={Identitydoctype}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Document Type"
                        // required
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Document Value{""}
                        <span className="text-red-600 ">*</span>
                      </label>
                      <input
                        type="text"
                        id="documentvalue"
                        disabled
                        defaultValue={Identitydocvalue}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter  Document Value"
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Document Image{""}
                        <span className="text-red-600 ">*</span>
                      </label>
                      <input
                        type="text"
                        id="Document Image"
                        disabled
                        defaultValue={IdentityProofImage}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Document Image"
                        // required
                      />
                    </div>
                  </div>
                  <div>
                    {""}
                    <h2 className="font-normal xs:font-bold px-3 mt-2">
                      Account Details
                    </h2>
                  </div>
                  {/* table */}
                  <div className="bg-white overflow-x-auto relative shadow-md  mt-2 ">
                    <div className="overflow-x-auto relative shadow-md  ">
                      <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
                        <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                          <tr>
                            <th scope="col" className="py-2.5 px-6">
                              Account Type
                            </th>
                            <th scope="col" className="py-2.5 px-6">
                              Account Number
                            </th>
                            <th scope="col" className="py-2.5 px-6">
                              ACCOUNT Status
                            </th>
                            <th scope="col" className="py-2.5 px-5 ">
                              ACCOUNT Balance
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          <>
                            {tableAlldata.map(
                              ({
                                strAccountType,
                                strAccountNumber,
                                strStatus,
                                strClosingBalance,
                              }) => (
                                <tr className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400">
                                  <td className="px-6 py-2.5 whitespace-nowrap">
                                    <span className="text-sm font-medium text-gray-900">
                                      {strAccountType || "-"}
                                    </span>
                                  </td>
                                  <td className="px-6 py-2.5 whitespace-nowrap">
                                    <span className="text-sm font-medium text-gray-900">
                                      {strAccountNumber || "-"}
                                    </span>
                                  </td>
                                  <td className="px-6 py-2.5 whitespace-nowrap">
                                    <span className="text-sm font-medium text-gray-900">
                                      {strStatus || "-"}
                                    </span>
                                  </td>
                                  <td className="px-6 py-2.5 whitespace-nowrap text-right">
                                    <span className="text-sm font-medium text-gray-900 ">
                                      {strClosingBalance || "-"}
                                    </span>
                                  </td>
                                </tr>
                              )
                            )}
                          </>
                        </tbody>
                      </table>
                    </div>
                  </div>

                  <div className="grid gap-6 mt-4 md:grid-cols-3 px-3 mt- ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Closing Balance{""}
                        <span className="text-red-600 ">*</span>
                      </label>
                      <input
                        type="text"
                        id="closinblc"
                        defaultValue={closinblc}
                        disabled
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Closing Balance"
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Maker Id <span className="text-red-600 ">*</span>
                      </label>
                      <input
                        type="text"
                        id="makerid"
                        defaultValue={makerid}
                        disabled
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Maker id"
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Reason For Closure{""}
                        <span className="text-red-600 ">*</span>
                      </label>
                      <input
                        type="text"
                        id="resclousing"
                        defaultValue={resclousing}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Reason For Closure"
                      />
                    </div>
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-2 text-right sm:px-6 ">
                <button
                  type="button"
                  data-modal-toggle="defaultModal"
                  onClick={approve}
                  className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
                >
                  Approve
                </button>
                <button
                  type="button"
                  onClick={reject}
                  className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
                >
                  Reject
                </button>
                <button
                  title="Go Back"
                  type="button"
                  onClick={() => navigate(-1)}
                  className="bg-gray-500 hover:bg-gray-700 focus:ring-2 focus:ring-gray-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-gray-700 rounded"
                >
                  Back
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>
      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
