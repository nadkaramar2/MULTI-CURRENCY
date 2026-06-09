import React, { useState, useEffect, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import Swal from "sweetalert2";
import logImage from "../../assets/img/TranEco.png";
import { useParams } from "react-router-dom";
import { Dialog, Transition } from "@headlessui/react";
import CustomAlert from "../../layout/CustomAlert";
import MainPagination from "../../layout/MianPagination";
export default function ApproveLinkAccount() {
  const [data8, setalldata8] = useState([]);
  const [alldata, setAlldata] = useState([]);
  const [showhide, setShowhide] = useState("");
  const [showhide1, setshowhide1] = useState("");
  const { strMobileNo } = useParams();
  const [customerdata, setCustomerdata] = useState({});
  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(1);
  const handlesshowhide = (event) => {
    const getuser = event.target.value;
    setShowhide(getuser);
  };
  const handles = (event) => {
    const getuser = event.target.value;
    setshowhide1(getuser);
  };

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

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
    customer();
  }, []);

  const CustId = customerdata.strCustId;
  localStorage.setItem("CustId", CustId);
  let CustID = localStorage.getItem("CustId");
  const ActiveTier = customerdata.strActiveTier;
  localStorage.setItem("ActiveTier", ActiveTier);
  let activeTier = localStorage.getItem("ActiveTier");
  const customer = async () => {
    try {
      const response = await amsApi.post(
        `customerId/getCustByMobileNumbr`,
        strMobileNo,
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setCustomerdata(response.data.customerIdCreation);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  useEffect(() => {
    tabledata();
  }, []);
  const tabledata = async () => {
    try {
      const response = await amsApi.post(
        `account/getAccountInfoListBasdOnTypes`,
        {
          strCustId: CustId,
        }
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.accountInfoList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  useEffect(() => {
    kycData();
  }, []);
  const kycData = async () => {
    try {
      const response = await amsApi.post(
        `account-wise-kyc-details/getSingleAccountKycDetails`,
        {
          strMobileNo: strMobileNo,
        }
      );
      if (response.data.code === "S0000") {
        setalldata8(response.data.accountKycDetails);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  //const [imagesData, setimages] = useState("")

  const imagesData = data8.strTier1PassportPhotograph;
  const BvnNumber = data8.strBvnNumber;
  let [isOpen, setIsOpen] = useState(false);

  function closeModal() {
    setIsOpen(false);
  }

  function openModal() {
    setIsOpen(true);
  }

  const [list, setList] = useState([]);

  useEffect(() => {
    accountdetail();
  }, [tcount]);
  const accountdetail = async () => {
    try {
      const response = await amsApi.post(
        `account/getAccountInfoListBasdOnTypes/pagination/${itemPage}/${tcount}`,
        {
          strCustId: CustId,
        }
      );
      if (response.data.code === "S0000") {
        setList(response.data.accountInfoList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Link Account
              </p>
            </div>
          </div>
        </div>
        <div className="md:col-span-2 lg:col-span-1  ">
          <div className="overflow-hidden shadow ">
            <div className=" px-4  sm:p-2 bg-white  ">
              <div className="grid gap-2 grid-cols-3 px-8 ">
                <div>
                  <label
                    htmlFor="GL Account Type"
                    className="block mb-2 text-xs font-medium text-gray-900 dark:text-white"
                  >
                    Customer Id
                  </label>
                  <input
                    type="text"
                    id="CustID"
                    value={CustID}
                    disabled
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-1.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="GL Account Type"
                    className="block mb-2 text-xs font-medium text-gray-900 dark:text-white"
                  >
                    Current Tier
                  </label>
                  <input
                    type="text"
                    id="activeTier"
                    value={activeTier}
                    disabled
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-1.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
              </div>
              <div className="grid gap-6 grid-cols-3 px-8 mt-10 ">
                <div>
                  <label
                    htmlFor="GL Account Type"
                    className="block mb-2 text-xs font-medium text-gray-900 dark:text-white"
                  >
                    Select Option to View KYC/Account Details
                  </label>
                  <select
                    id="accountType"
                    name="accountType"
                    onChange={(e) => handlesshowhide(e)}
                    // value={transactionData1}
                    // onChange={(e) => settransactionData1(e.target.value)}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-1.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    required
                  >
                    <option value="">Select</option>
                    <option value="KYC">Show KYC</option>
                    <option value="Details">Show Account Details</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </div>

        {showhide === "KYC" && (
          <div className="grid gap-4 mb-2 md:grid-cols-4 mt-10 px-8 ">
            <div>
              <label
                htmlFor="text"
                className="block  text-xs font-medium text-gray-900 dark:text-white px-4 "
              >
                Tier 1 : Passport Photograph
                <span className="text-red-600">*</span>
              </label>
              <button
                title="Click Here"
                type="button"
                onClick={openModal}
                className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2 px-5 mx-4 text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 mt-2"
              >
                Tier1PassportPhotograph
              </button>
              {/* <input
                  type="text"
                  // value={strTier1PassportPhotograph}
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  required
                /> */}
            </div>
            <div>
              <label
                htmlFor="text"
                className="block  text-xs font-medium text-gray-900 dark:text-white"
              >
                BVN Number<span className="text-red-600">*</span>
              </label>
              <input
                type="text"
                id="BvnNumber"
                defaultValue={BvnNumber}
                // onChange={(e) => setstrBvnNumber(e.target.value)}
                className="bg-gray-50 border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 mt-2"
                required
              />
            </div>
            <div>
              <label
                htmlFor="text"
                className="block  text-xs font-medium text-gray-900 dark:text-white"
              >
                Tier 2 : Passport Photograph
                <span className="text-red-600">*</span>
              </label>
              {/* <Link to=""> */}
              <input
                type="text"
                onClick={openModal}
                // value={strTier2PassportPhotograph}
                className="bg-gray-50 border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 mt-2"
                required
              />
              {/* </Link> */}
            </div>
          </div>
        )}
        {showhide === "Details" && (
          <div className="mt-12 px-8">
            <div>
              <h2 className="font-normal md:font-bold">Account Deatils</h2>
              <br />
            </div>
            <div className="overflow-x-auto relative shadow-md sm:rounded-lg">
              <div className="table-wrp block max-h-[27rem] ">
                <table className="w-full text-xs text-left text-clack dark:text-blue-100">
                  <thead className="text-xs border-b sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
                    <th scope="col" className="py-2.5 px-6">
                      A/C Type
                    </th>
                    <th scope="col" className="py-2.5 px-6">
                      A/C Number
                    </th>
                    <th scope="col" className="py-2.5 px-6">
                      A/C Status
                    </th>
                    <th scope="col" className="py-2.5 px-6">
                      A/C Balance
                    </th>
                  </thead>
                  <tbody>
                    <>
                      {alldata.map(
                        ({
                          strAccountType,
                          strAccountNumber,
                          strStatus,
                          strClosingBalance,
                        }) => (
                          <tr
                            //   key={uuidv4()}
                            className=""
                          >
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xsclassName text-gray-900">
                                {strAccountType || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xsclassName text-gray-900">
                                {strAccountNumber || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xsclassName text-gray-900">
                                {strStatus || "-"}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xsclassName text-gray-900">
                                {strClosingBalance || "-"}
                              </div>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        )}
        <div className="grid gap-6 grid-cols-3 px-8 mt-12 ">
          <div>
            <label
              htmlFor="GL Account Type"
              className="block mb-2 text-xs font-medium text-gray-900 dark:text-white"
            >
              Select Account Number Option
              <span className="text-red-600">*</span>
            </label>
            <select
              id="accountNumber"
              name="accountType"
              onChange={(e) => handles(e)}
              // value={transactionData1}
              // onChange={(e) => settransactionData1(e.target.value)}
              className="bg-gray-50 border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500 mi-2"
              required
            >
              <option>Select</option>

              <option value="Instant">Show Instant Account List</option>
              <option value="AccountDetails">Show Account Details</option>
            </select>
          </div>
        </div>

        {showhide1 === "Instant" && (
          <div className="mt-12 px-8">
            <div className="overflow-x-auto relative shadow-md sm:rounded-lg">
              <div className="table-wrp block max-h-[27rem] ">
                <table className="w-full text-xs text-left text-clack dark:text-blue-100">
                  <thead className="text-xs border-b sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
                    <th scope="col" className="py-2.5 px-6">
                      A/C Type
                    </th>
                    <th scope="col" className="py-2.5 px-6">
                      A/C Number
                    </th>
                    <th scope="col" className="py-2.5 px-6">
                      A/C Status
                    </th>
                    <th scope="col" className="py-2.5 px-6">
                      A/C Balance
                    </th>
                  </thead>
                  <tbody>
                    <>
                      {alldata.map(
                        ({
                          strAccountType,
                          strAccountNumber,
                          strStatus,
                          strClosingBalance,
                        }) => (
                          <tr
                            //   key={uuidv4()}
                            className=""
                          >
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xsclassName text-gray-900">
                                {/* {strAccountType || "-"} */}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xsclassName text-gray-900">
                                {/* {strAccountNumber || "-"} */}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xsclassName text-gray-900">
                                {/* {strStatus || "-"} */}
                              </div>
                            </td>
                            <td className="px-6 py-2.5 whitespace-nowrap">
                              <div className="text-xsclassName text-gray-900">
                                {/* {strClosingBalance || "-"} */}
                              </div>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  </tbody>
                </table>
              </div>
            </div>
            <MainPagination itemPageData={setItemPage} tCountData={setTcount} />
          </div>
        )}

        {showhide1 === "AccountDetails" && (
          <div onClick={accountdetail}>
            <div className="grid gap-6 grid-cols-3 px-8 mt-12 ">
              {list.map((data) => (
                <div>
                  <label
                    htmlFor="text"
                    className="block  text-xs font-medium text-gray-900 dark:text-white"
                  >
                    Selected Account Type
                    <span className="text-red-600">*</span>
                  </label>
                  <input
                    type="text"
                    id="strAccountType"
                    name="strAccountType"
                    defaultValue={data.strAccountType}
                    disabled
                    // value={strAccountType}
                    // onChange={(e) => settransactionData1(e.target.value)}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    //   placeholder="Amount"
                    required
                  />
                </div>
              ))}
              <div>
                <label
                  htmlFor="text"
                  className="block  text-xs font-medium text-gray-900 dark:text-white"
                >
                  Generated Account Number
                </label>
                <input
                  type="text"
                  id="strAccountNumber"
                  name="strAccountNumber"
                  // defaultValue={data.strAccountNumber}
                  disabled
                  // onChange={(e) => setstrAccountNumber(e.target.value)}
                  className="bg-gray-50 border border-gray-300 text-gray-900 text-xs rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                  //   placeholder="Amount"
                  required
                />
              </div>
            </div>
          </div>
        )}

        <div className="bg-gray-100 px-4 py-3 text-right sm:px-6 mt-3 ">
          <button
            title="Save Here"
            type="button"
            data-modal-toggle="defaultModal"
            // onClick={txn}
            className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2 px-5 mx-4 text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
          >
            Save Account Number
          </button>
        </div>
      </div>
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}

      <Transition appear show={isOpen} as={Fragment}>
        <Dialog as="div" className="relative z-12" onClose={closeModal}>
          <Transition.Child
            as={Fragment}
            enter="ease-out duration-300"
            enterFrom="opacity-0"
            enterTo="opacity-100"
            leave="ease-in duration-200"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
          >
            <div className="fixed inset-0 bg-black bg-opacity-25" />
          </Transition.Child>

          <div className="fixed inset-0 overflow-y-auto">
            <div className="flex min-h-full items-center justify-center p-4 text-center">
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0 scale-95"
                enterTo="opacity-100 scale-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100 scale-100"
                leaveTo="opacity-0 scale-95"
              >
                <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                  <Dialog.Title
                    as="h3"
                    className="text-lg font-medium leading-6 text-gray-900"
                  >
                    Passport Photograph1
                  </Dialog.Title>
                  <div className="mt-2">
                    <img src={logImage} alt="Image" />
                  </div>

                  <div className="mt-4">
                    <button
                      title="Close Here"
                      type="button"
                      className="inline-flex justify-center rounded-md border border-transparent bg-blue-100 px-4 py-2 text-xs font-medium text-blue-900 hover:bg-blue-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-blue-500 focus-visible:ring-offset-2"
                      onClick={closeModal}
                    >
                      Close
                    </button>
                  </div>
                </Dialog.Panel>
              </Transition.Child>
            </div>
          </div>
        </Dialog>
      </Transition>
    </AppLayout>
  );
}
