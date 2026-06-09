import React, { useState, useEffect, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { Dialog, Transition } from "@headlessui/react";
import { ExclamationCircleIcon } from "@heroicons/react/24/solid";
import swal from "sweetalert";
import CustomAlert from "../../layout/CustomAlert";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";

export default function CloseAccpountForm() {
  let participantID = sessionStorage.getItem("Participantid");
  const accessToken = localStorage.getItem("token");
  const [openClickTogle, setOpenClickTogle] = useState(false);
  const [alldata, setalldata] = useState([]);
  const [alldata2, setalldata2] = useState([]);
  const [setalldata3, setAlldata3] = useState([]);
  const [setalldata5, setAlldata5] = useState([]);
  const [strAccountType, setstrAccountType] = useState("");
  const [bankList, setbankList] = useState("");
  const [showhide, setShowhide] = useState("");
  const [alldatatype, setAlldatatype] = useState([]);
  const [alldatatype1, setAlldatatype1] = useState({});
  const [recpntName, setRecpntName] = useState("");
  const [custid, setCustid] = useState("");
  const [bankCode, setbankCode] = useState("");
  const [accountType, setAccountType] = useState("");
  const [accountnumber, setAccountNumber] = useState("");
  const [strAccountHolderName, setstrAccountHolderName] = useState("");
  console.log(strAccountHolderName);
  const [strCurrentrTier, setstrCurrentrTier] = useState("");
  console.log(strCurrentrTier);
  const [closureReason, setclosureReason] = useState("");

  const [balanceTransferTo, setbalanceTransferTo] = useState("");
  console.log("gfhgfh" + balanceTransferTo);
  const [recipientBankId, setrecipientBankId] = useState("");
  const [recipientAccountHolderName, setRecipientAccountHolderName] =
    useState("");
  console.log(recipientAccountHolderName);
  // const [transferAmount, settransferAmount] = useState("");
  // const [strRequestBtn, setstrRequestBtn] = useState("");
  // const [strAccountNumber, setstrAccountNumber] = useState("");

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [newstrAccountNo, setNewstrAccountNo] = useState("");
  const [newstrAccountType, setNewstrAccountType] = useState("");
  const [newstrAvailableBalance, setNewstrAvailableBalance] = useState("");
  console.log(newstrAvailableBalance);
  const getAccountvalues = (
    strAccountNo,
    strAccountType,
    strAvailableBalance
  ) => {
    setNewstrAccountNo(strAccountNo);
    setNewstrAccountType(strAccountType);
    setNewstrAvailableBalance(strAvailableBalance);
  };

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

  const [atype, setAtype] = useState("");
  console.log(atype);
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (custid === "") {
      swal("Please Enter Customer ID");
    } else {
      try {
        const response = await amsApi.post(
          `account/getAccountInfoForClosingAccount`,
          {
            strCustId: custid,
          }
        );
        if (response.status === 200) {
          setAlldatatype(response.data);
          setAlldatatype1(response.data[0]);
          setAtype(response.data.strAccountType[0]);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  //account trype
  useEffect(() => {
    categoryListModelsList();
  }, [strAccountType]);

  const categoryListModelsList = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getNonCreditAccounType`,
        {}
      );
      if (response.data.code === "S0000") {
        setalldata(response.data.message);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };
  // get Account Number
  useEffect(() => {
    AccountNumber();
  }, []);

  const AccountNumber = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getNonCrditAccounType`,
        {}
      );

      if (response.data.code === "S0000") {
        setAlldata3(response.data.accountTypeMasterlistData);
        // showSuccess(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  const [recipiantname, setRecipiantname] = useState("");
  console.log(recipiantname);
  const Reciholdername = setalldata5.map((t) => [t.strAccountHolderName])[0];
  const handlename = (e) => {
    const Reciholdername = e.target.value;
    setRecipiantname(Reciholdername);
  };

  useEffect(() => {
    AccTypenumber();
  }, [accountnumber]);
  const AccTypenumber = async () => {
    try {
      const response = await amsApi.post(`account/getAccountBalancAndName`, {
        strAccountType: accountType,
        strAccountNumber: accountnumber,
      });
      if (response.data.code === "S0000") {
        setAlldata5(response.data.accountInfoList);
        setRecpntName(response.data.accountInfoList[0].strAccountHolderName);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };
  // bank List
  useEffect(() => {
    BankList();
  }, [bankList]);

  const BankList = async () => {
    try {
      const response = await amsApi.post(
        `thirdParty/getThirdPartyBankList`,
        {}
      );
      if (response.data.code === "S0000") {
        setalldata2(response.data.banksList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };
  // save form

  const closeaccountSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await amsApi.post(
        `accountclouser/request`,
        {
          strParticipantId: participantID,
          strCustId: custid,
          accountHolderName: alldatatype1.strAccountHolderName,
          currentAccountTier: strCurrentrTier,
          accountDetails_length: "10",
          closureReason: closureReason,
          balanceTransferTo: balanceTransferTo,
          recipientBankId,
          currentAccountBalance: newstrAvailableBalance,
          strAccountType: newstrAccountType,
          strAccountNumber: newstrAccountNo,
          recipientAccountType: accountType,
          recipientAccountNo: accountnumber,
          recipientAccountHolderName: recpntName,
          transferAmount: newstrAvailableBalance,
          strRequestBtn: "1",
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
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  const handlesshowhide = (event) => {
    const getuser = event.target.value;

    setbalanceTransferTo(getuser);
    setShowhide(getuser);
  };
  // modal function
  let [isOpen, setIsOpen] = useState(false);
  function closeModal() {
    setIsOpen(false);
  }

  function openModal(id) {
    setIsOpen(true);
  }
  const openClickHandler = () => {
    setOpenClickTogle(true);
    closeModal();
  };

  return (
    <AppLayout>
      <div className="mx-4">
        <div className="w-full shadow-md mt-2">
          <div className="px-4 sm:px-10  bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-sm  sm:text-xm text-black :text-2xl  leading-normal ">
                Close Account
              </p>
            </div>
          </div>
          <div className="max-w-7xl mx-auto h-full overflow-auto max-h-full lg:max-h-[32rem]">
            <form className="">
              <div className="overflow-hidden shadow ">
                <div className=" px-4 sm:p-2 bg-white ">
                  <div className="grid gap-2 mb-2 md:grid-cols-4">
                    <div>
                      <label className="block  text-xs font-medium text-gray-900 dark:text-white">
                        Customer Id <span className="text-red-600">*</span>
                        {/* 23000001297 23000004297 23000030299 Nikhil */}
                      </label>

                      <input
                        type="text"
                        id="custid"
                        value={custid}
                        onChange={(e) => setCustid(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Cust Id"
                      />
                    </div>
                    <div className="my-4">
                      <button
                        title="Click Search Button"
                        type="submit"
                        onClick={handleSubmit}
                        className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800   focus:ring-offset-2 text-white font-md py-2 px-5 border border-blue-700 rounded  shadow-lg inner"
                      >
                        <MagnifyingGlassIcon className="h-3 w-3" />
                      </button>
                    </div>
                  </div>
                  <div className="grid gap-4  md:grid-cols-3 ">
                    {alldatatype1 && (
                      <>
                        <div>
                          <label
                            htmlFor="GL Account Description"
                            className="block mb-1  text-xs font-medium text-gray-900 dark:text-white"
                          >
                            Customer Name
                          </label>

                          <input
                            type="text"
                            id="strAccountHolderName"
                            defaultValue={alldatatype1.strAccountHolderName}
                            onChange={(e) =>
                              setstrAccountHolderName(e.target.value)
                            }
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          />
                        </div>
                        <div>
                          <label
                            htmlFor="strCurrentrTier"
                            className="block mb-1 text-xs font-medium text-gray-900 dark:text-white"
                          >
                            Current Tier
                          </label>

                          <input
                            type="text"
                            id="strCurrentrTier"
                            defaultValue={alldatatype1.strCurrentrTier}
                            onChange={(e) => setstrCurrentrTier(e.target.value)}
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          />
                        </div>
                        <div>
                          <label
                            htmlFor="GL Account Description"
                            className="block mb-1 text-xs font-medium text-gray-900 dark:text-white"
                          >
                            Tier 1 : Passport Photograph
                          </label>

                          <input
                            type="text"
                            id="strTier1PassportPhotograph"
                            defaultValue={
                              alldatatype1.strTier1PassportPhotograph
                            }
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          />
                        </div>
                      </>
                    )}
                  </div>
                </div>
              </div>
            </form>

            {/* table */}
            <div className="bg-white overflow-x-auto relative shadow-md  mt-1">
              <div className="overflow-x-auto relative shadow-md  ">
                <div>
                  <h2 className="font-normal md:font-bold px-4 py-1  ">
                    List Of Account For Closing
                  </h2>
                </div>
                <table className="w-full  text-xs text-left text-blue-100 dark:text-blue-100">
                  <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                    <tr>
                      <th scope="col" className="py-2.5 px-6">
                        ACCOUNT TYPE
                      </th>
                      <th scope="col" className="py-2.5 px-6">
                        ACCOUNT NUMBER
                      </th>
                      <th scope="col" className="py-2.5 px-6">
                        AVAILABLE BALANCE
                      </th>
                      <th scope="col" className="py-2.5 px-6">
                        ACTION
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    {alldatatype.map((data) => (
                      <>
                        <tr className="bg-blue-00 border-b  hover:bg-blue-200 ">
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <span className=" text-xs font-medium text-gray-900">
                              {data.strAccountType || "-"}
                            </span>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <span className=" text-xs font-medium text-gray-900">
                              {data.strAccountNo || "-"}
                            </span>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <span className=" text-xs font-medium text-gray-900">
                              {data.strAvailableBalance || "-"}
                            </span>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <span
                              className=" text-xs font-medium text-blue-600"
                              onClick={openModal}
                            >
                              <button
                                title="Click Here To Link"
                                type="button"
                                onClick={(e) =>
                                  getAccountvalues(
                                    data.strAccountNo,
                                    data.strAccountType,
                                    data.strAvailableBalance
                                  )
                                }
                              >
                                <span text={"red"}>
                                  Click here to Close Account
                                </span>
                              </button>
                            </span>
                          </td>
                        </tr>
                      </>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
            <div className="mt-2">
              {openClickTogle && (
                <>
                  <div>
                    <h2 className="font-normal md:font-bold px-4 py-1">
                      Other Information
                    </h2>
                  </div>
                  <hr></hr>
                  <div className="bg-white px-4 py-1 ">
                    <label
                      for="password"
                      className="block   text-xs font-medium text-gray-900 dark:text-white"
                    >
                      Closure Reason
                    </label>
                    <textarea
                      id="closureReason"
                      rows=""
                      value={closureReason}
                      onChange={(e) => setclosureReason(e.target.value)}
                      className="block p-3.5 w-4/12  text-xs text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                      placeholder="Write Closure Reason Heare..."
                    />
                  </div>
                  <div className="grid gap-6 grid-cols-3  px-4 mt-1 ">
                    <div>
                      <label
                        htmlFor="GL Account Type"
                        className="block  text-xs font-medium text-gray-900 dark:text-white"
                      >
                        Transfer Option <span className="text-red-600">*</span>
                      </label>

                      <select
                        id="accountNumber"
                        name="accountType"
                        value={balanceTransferTo}
                        onChange={(e) => handlesshowhide(e)}
                        // value={transactionData1}
                        // onChange={(e) => settransactionData1(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        <option value="IntLinkedAcc">
                          Internal Linked account
                        </option>
                        <option value="InternalAccount">
                          Internal account
                        </option>
                        <option value="ExternalAccount">
                          External account
                        </option>
                      </select>
                    </div>
                  </div>
                  {showhide === "IntLinkedAcc" && (
                    <div className=" grid gap-6 grid-cols-3 mt-5 px-4">
                      <div>
                        <label
                          htmlFor="text"
                          className="block   text-xs font-medium text-gray-900 dark:text-white"
                        >
                          Sealected Account type
                          <span className="text-red-600">*</span>
                        </label>

                        <select
                          name="strAccountType"
                          id="strAccountType"
                          value={strAccountType}
                          onChange={(e) => setstrAccountType(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        >
                          <option value="">Select</option>
                          {alldata.map((data) => (
                            <optgroup key={uuidv4()}>
                              <option
                                className="capatlize text-lg"
                                // value={}
                              >
                                {data.strAccountType || "-"}
                              </option>
                            </optgroup>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block   text-xs font-medium text-gray-900 dark:text-white"
                        >
                          Selected Account No
                        </label>

                        <input
                          type="text"
                          // value={amount}
                          // onChange={(e) => setAmount(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        />
                      </div>
                    </div>
                  )}

                  {showhide === "InternalAccount" && (
                    <div
                      id="account"
                      className=" grid gap-6  grid-cols-3 mt-2 px-4"
                    >
                      <div>
                        <label
                          htmlFor="text"
                          className="block   text-xs font-medium text-gray-900 dark:text-white"
                        >
                          Account Type <span className="text-red-600">*</span>
                        </label>
                        <select
                          name="accountType"
                          id="accountType"
                          value={accountType}
                          onChange={(e) => setAccountType(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          required
                        >
                          <option value="">Select</option>
                          {setalldata3.map((data) => (
                            <option
                              className="capatlize text-lg"
                              value={data.strAccountType}
                            >
                              {data.strAccountType}-{data.strDescription}
                            </option>
                          ))}
                        </select>
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block   text-xs font-medium text-gray-900 dark:text-white"
                        >
                          Account No
                        </label>
                        <input
                          type="text"
                          value={accountnumber}
                          onChange={(e) => setAccountNumber(e.target.value)}
                          onKeyUp={AccTypenumber}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Account No"
                        />
                      </div>
                      {/* {setalldata5.map((data) => ( */}
                      <>
                        <div>
                          <label
                            htmlFor="text"
                            className="block   text-xs font-medium text-gray-900 dark:text-white"
                          >
                            Account Holder Name
                          </label>
                          <input
                            type="text"
                            value={Reciholdername}
                            onChange={handlename}
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                            placeholder="Enter Account Holder Name"
                            required
                          />
                        </div>
                      </>
                      {/* ))} */}
                    </div>
                  )}

                  {showhide === "ExternalAccount" && (
                    <div className=" grid gap-6  grid-cols-4 mt-2 px-4">
                      <div>
                        <label
                          htmlFor="text"
                          className="block   text-xs font-medium text-gray-900 dark:text-white "
                        >
                          Bank List <span className="text-red-600">*</span>
                        </label>
                        <select
                          name="bankList"
                          id="bankList"
                          value={bankList}
                          onChange={(e) => setbankList(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          required
                        >
                          <option value="">Select</option>
                          {alldata2.map((data) => (
                            <option
                              className="capatlize text-lg"
                              // value={}
                            >
                              {data.bankName || "-"}
                            </option>
                          ))}
                        </select>
                      </div>
                      {alldata2.map((data) => (
                        <>
                          <div>
                            <label
                              htmlFor="text"
                              className="block   text-xs font-medium text-gray-900 dark:text-white"
                            >
                              Selected Bank Code
                            </label>
                            <input
                              type="text"
                              value={data.bankCode}
                              onChange={(e) => setbankCode(e.target.value)}
                              className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                              placeholder="Enter Bank Code"
                              required
                            />
                          </div>
                        </>
                      ))}
                      <div>
                        <label
                          htmlFor="text"
                          className="block   text-xs font-medium text-gray-900 dark:text-white"
                        >
                          Account No
                        </label>
                        <input
                          type="text"
                          // value={amount}
                          // onChange={(e) => setAmount(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter Account No"
                          required
                        />
                      </div>
                      <div>
                        <label
                          htmlFor="text"
                          className="block   text-xs font-medium text-gray-900 dark:text-white"
                        >
                          Account Holder Name
                        </label>
                        <input
                          type="text"
                          // value={amount}
                          // onChange={(e) => setAmount(e.target.value)}
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          placeholder="Enter account Holder Name"
                          required
                        />
                      </div>
                    </div>
                  )}
                </>
              )}
            </div>

            {/* <hr></hr> */}
            <div>
              <h2 className="font-normal mt-2 md:font-bold px-4 py-1">
                Transferring Amount
              </h2>
            </div>
            {/* <hr></hr> */}
            <div className="grid gap-2 mb-2 md:grid-cols-3 py-1 px-1">
              <div className="mx-3">
                <label
                  htmlFor="transferamount"
                  className="block mb-1  text-xs font-medium text-gray-900 dark:text-white px-1"
                >
                  Transfer Amount
                </label>

                <input
                  type="text"
                  disabled
                  id="transferamount"
                  defaultValue={newstrAvailableBalance}
                  className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                  placeholder="Enter Transfer Amount"
                  required
                />
              </div>
            </div>
          </div>
          <div className="bg-gray-100 px-4  py-1 text-right sm:px-2 ">
            <button
              type="button"
              onClick={closeaccountSubmit}
              className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded mx-6"
            >
              Submit
            </button>
          </div>
        </div>
      </div>
      {/* </div>
      </div> */}

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* model */}
      <Transition appear show={isOpen} as={Fragment}>
        <Dialog
          as="div"
          className="fixed inset-0 z-10 overflow-y-auto "
          onClose={() => {}}
        >
          <div className="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
            <Transition.Child
              as={Fragment}
              enter="ease-out duration-200"
              enterFrom="opacity-0"
              enterTo="opacity-100"
              leave="ease-in duration-200"
              leaveFrom="opacity-100"
              leaveTo="opacity-0"
            >
              <Dialog.Overlay className="fixed inset-0" />
            </Transition.Child>

            <span
              className="inline-block h-screen  align-middle"
              aria-hidden="true"
            >
              &#8203;
            </span>
            <Transition.Child
              as={Fragment}
              enter="ease-out duration-300"
              enterFrom="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
              enterTo="opacity-100 translate-y-0 sm:scale-100"
              leave="ease-in duration-200"
              leaveFrom="opacity-100 translate-y-0 sm:scale-100"
              leaveTo="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
            >
              <div className="relative inline-block align-middle bg-white rounded-lg text-left overflow-hidden shadow-md transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
                <div className="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
                  <div className="sm:flex sm:items-start">
                    <div className="mx-auto flex-shrink-0 flex items-center justify-center h-12 w-12 rounded-full bg-red-100 sm:mx-0 sm:h-10 sm:w-10">
                      <ExclamationCircleIcon
                        className="h-4 w-4 text-red-600"
                        aria-hidden="true"
                      />
                    </div>
                    <div className="mt-2 text-center sm:mt-0 sm:ml-4 sm:text-left">
                      <Dialog.Title
                        as="h3"
                        className="text-md leading-6 font-medium text-gray-900"
                      >
                        Close Account
                      </Dialog.Title>
                      <div className="mt-1">
                        <p className=" text-xs text-gray-900">
                          Do You Want To Close This Account!
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
                <div className="px-2 py-1 text-right sm:px-8 ">
                  <button
                    type="button"
                    onClick={openClickHandler}
                    className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1 px-3 mx-4  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                  >
                    OK
                  </button>
                  <button
                    type="button"
                    onClick={closeModal}
                    className="inline-flex justify-center rounded-md border border-transparent bg-red-400 py-1 px-3 mx-4  text-xs font-medium text-white shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2"
                  >
                    Clear
                  </button>
                </div>
              </div>
            </Transition.Child>
          </div>
        </Dialog>
      </Transition>
    </AppLayout>
  );
}
